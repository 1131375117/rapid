package cn.huacloud.taxpreference.services.backup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: fuhua
 * @create: 2021-11-15 17:44
 **/
@Component
public class JiLinTax implements Tax{
    @Override
    public String sourceType() {
        return "国家税务总局吉林省税务局";
    }

    @Override
    public String parseHtml(String html) {
        Document parse = Jsoup.parse(html);
        Element zoom = parse.getElementById("zoom");
        if(zoom==null){
            return "";
        }
        return String.valueOf(zoom);
    }
}
