package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author wangkh
 */
public interface ElasticsearchConsumer<T extends IDGetter<?>> {

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
}
