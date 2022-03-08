package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.consumer.entity.AbstractCombinePlainContent;
import cn.huacloud.taxpreference.services.consumer.entity.CombineText;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.message.impl.TencentSmsServiceImpl;
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
public interface ElasticsearchConsumer<T extends IDGetter<?>> {

    Logger getLog();

    String getIndex();

    default RestHighLevelClient getRestHighLevelClient() {
        return SpringUtil.getBean(RestHighLevelClient.class);
    }

    default SmsService getSmsServer() {
        return SpringUtil.getBean(TencentSmsServiceImpl.class);
    }

    default ObjectMapper getObjectMapper() {
        return SpringUtil.getBean(ObjectMapper.class);
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
     *
     * @param source ES数据实体
     */
    default void setCombinePlainText(T source) {
        if (!(source instanceof AbstractCombinePlainContent)) {
            return;
        }

        // 类型强制转换
        AbstractCombinePlainContent<?> combineTextSource = (AbstractCombinePlainContent<?>) source;

        // 初始化其他组合字段
        combineTextSource.initialOtherCombineFields();

        // 设置组合字段
        List<CombineText> combineTexts = combineTextSource.combineTextList();
        if (CollectionUtils.isEmpty(combineTexts)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (CombineText combineText : combineTexts) {
            if (sb.length() > 0) {
                // 换行
                sb.append(" ");
            }
            String text = combineText.getText();
            if (combineText.getHtml()) {
                if (text != null) {
                    String plainText = getPlainText(source, text);
                    sb.append(plainText);
                }
            } else {
                if (text != null) {
                    sb.append(text);
                }
            }
        }
        combineTextSource.setCombinePlainContent(sb.toString());
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
