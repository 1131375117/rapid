package cn.huacloud.taxpreference.services.backup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * @description: 内蒙古税务局正文获取
 * @author: fuhua
 * @create: 2021-11-11 10:31
 **/
@Component
public class NeiMengGuTax implements Tax {
    @Override
    public String sourceType() {
        return "国家税务总局内蒙古自治区税务局";
    }

    @Override
    public String parseHtml(String html) {
        Document parse = Jsoup.parse(html);
        Element fontZoom = parse.getElementById("sdecontent");
        return String.valueOf(fontZoom);
    }
/*    public String parseQA(String html) {
        Document parse = Jsoup.parse(html);
        Element fontZoom = parse.getElementById("sdecontent");
        return String.valueOf(fontZoom);
    }*/

}
