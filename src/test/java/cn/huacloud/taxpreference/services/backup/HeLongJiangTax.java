package cn.huacloud.taxpreference.services.backup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * @description: 河北税务局正文获取
 * @author: fuhua
 * @create: 2021-11-11 10:31
 **/
@Component
public class HeLongJiangTax implements Tax {
    @Override
    public String sourceType() {
        return "国家税务总局黑龙江省税务局";
    }

    @Override
    public String parseHtml(String html) {
        Document parse = Jsoup.parse(html);
        Element fontZoom = parse.getElementById("zoom");
        if(fontZoom==null){
            return "";
        }
        return String.valueOf(fontZoom);
    }


}
