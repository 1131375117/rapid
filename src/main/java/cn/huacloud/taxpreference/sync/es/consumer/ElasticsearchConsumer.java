package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtils;
import cn.huacloud.taxpreference.services.consumer.entity.AbstractCombinePlainContent;
import cn.huacloud.taxpreference.services.consumer.entity.CombineText;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author wangkh
 */
public interface ElasticsearchConsumer<T extends AbstractCombinePlainContent<?>> {

    Logger getLog();

    String getIndex();

    default RestHighLevelClient getRestHighLevelClient() {
        return SpringUtils.getBean(RestHighLevelClient.class);
    }

    default ObjectMapper getObjectMapper() {
        return SpringUtils.getBean(ObjectMapper.class);
    }

    default void save(T source) {
        String index = getIndex();
        try {
            // 设置组合文本
            setCombinePlainText(source);
            IndexRequest indexRequest = new IndexRequest(index)
                    .id(source.getId().toString())
                    .source(getObjectMapper().writeValueAsString(source), XContentType.JSON);
            getRestHighLevelClient().index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            getLog().error("ES保存数据失败，index：{}，id：{}", index, source.getId());
        }
    }

    default void delete(Object id) {
        String index = getIndex();
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index)
                    .id(id.toString());
            getRestHighLevelClient().delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            getLog().error("ES删除数据失败，index：{}，id：{}", index, id);
        }
    }

    /**
     * 设置组合文本
     * @param source ES数据实体
     */
    default void setCombinePlainText(T source) {
        List<CombineText> combineTexts = source.combineTextList();
        if (CollectionUtils.isEmpty(combineTexts)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (CombineText combineText : combineTexts) {
            if (sb.length() > 0) {
                // 换行
                sb.append("\n");
            }
            if (combineText.getHtml()) {
                String plainText = getPlainText(source, combineText.getText());
                sb.append(plainText);
            } else {
                sb.append(combineText.getText());
            }
        }
        source.setCombinePlainContent(sb.toString());
    }

    /**
     * 抽取纯文本
     */
    default String getPlainText(T source, String html) {
        try {
            return Jsoup.parse(html).text();
        } catch (Exception e) {
            String name = source.getClass().getSimpleName();
            Object id = source.getId();
            getLog().error("html文本抽取失败，className：{}，id：{}，html：{}", name, id, html);
            return "";
        }
    }

}
