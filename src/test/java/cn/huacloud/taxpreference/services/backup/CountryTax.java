package cn.huacloud.taxpreference.services.backup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @description: 国家税务局正文获取
 * @author: fuhua
 * @create: 2021-11-11 10:31
 **/
@Component
public  class CountryTax implements Tax {
    @Override
    public String sourceType() {
        return "国家税务总局";
    }

    @Override
    public String parseHtml(String html) {
        Document parse = Jsoup.parse(html);
        Element fontZoom = parse.getElementById("fontzoom");
        Elements elements = fontZoom.select("[class=jiuc]");
        if(elements!=null){
            elements.remove();
        }

        return String.valueOf(fontZoom);
    }


}
