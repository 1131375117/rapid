package cn.huacloud.taxpreference.services.backup;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface Tax {

    String sourceType();

    String parseHtml(String html);

    default String parseQA(String html) {
        Document parse = Jsoup.parse(html);
        Element fontZoom = parse.getElementById("zoom");
        return String.valueOf(fontZoom);
    }
}
