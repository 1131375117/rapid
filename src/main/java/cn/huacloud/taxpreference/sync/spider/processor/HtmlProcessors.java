package cn.huacloud.taxpreference.sync.spider.processor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * html文本处理器
 * @author wangkh
 */
public interface HtmlProcessors {

    String STYLE_KEY = "style";

    String CLASS_KEY = "class";

    /**
     * 正文内容处理器
     */
    Function<String, Document> content = html -> {
        if (StringUtils.isBlank(html)) {
            return Jsoup.parse("");
        }
        return Stream.of(Jsoup.parse(html))
                // 先不去除样式，看具体需求
                //.map(HtmlProcessors.cleanStyle)
                //.map(HtmlProcessors.cleanClass)
                .map(HtmlProcessors.cleanScript)
                .findFirst().get();
    };

    /**
     * 去除html style样式
     */
    Function<Document, Document> cleanStyle = document -> {
        document.getElementsByAttribute(STYLE_KEY)
                .forEach(element -> element.removeAttr(STYLE_KEY));
        return document;
    };

    /**
     * 去除html class属性
     */
    Function<Document, Document> cleanClass = document -> {
        document.getElementsByAttribute(CLASS_KEY)
                .forEach(element -> element.removeAttr(CLASS_KEY));
        return document;
    };

    /**
     * 除去script标签
     */
    Function<Document, Document> cleanScript = document -> {
        document.select("script").forEach(Node::remove);
        return document;
    };
}
