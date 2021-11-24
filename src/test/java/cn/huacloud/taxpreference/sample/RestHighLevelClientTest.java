package cn.huacloud.taxpreference.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wangkh
 */
@Slf4j
public class RestHighLevelClientTest {
    static String hostname = "172.16.122.17";
    static Integer port = 9200;
    static String scheme = "http";
    static RestHighLevelClient client;


    @Test
    public void testQuery() throws Exception {
        SearchRequest request = new SearchRequest("policies_dev", "policies_explain_dev");
        request.source(SearchSourceBuilder.searchSource().size(1));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        log.info("OK");
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, scheme)));
    }

    @AfterClass
    public static void afterClass() throws Exception {
        client.close();
    }

}
