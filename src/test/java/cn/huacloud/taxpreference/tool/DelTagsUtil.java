package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.services.backup.HeBeiTax;

import java.io.IOException;

/**
 * 去除文章内容页页面代码里的HTML标签
 * Created by fuhua
 */
public class DelTagsUtil {
    /**
     * 去除html代码中含有的标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHtmlTags(String htmlStr) {
        //定义script的正则表达式，去除js可以防止注入
        String scriptRegex = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex = "<[^>]+>";
        //定义空格,回车,换行符,制表符
        String spaceRegex = "\\s*|\t|\r|\n";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(spaceRegex, "");
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 获取HTML代码里的内容
     *
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr) {
        //去除html标签
        htmlStr = delHtmlTags(htmlStr);
        //去除空格" "
        htmlStr = htmlStr.replaceAll(" ", "");
        return htmlStr;
    }

    public static void main(String[] args) throws IOException {
        HeBeiTax heBeiTax = new HeBeiTax();
        String s1 = heBeiTax.parseHtml("<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"renderer\" content=\"webkit\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1.0,user-scalable=0\">\n" +
                "    <meta name=\"description\" content=\"国家税务总局山西省税务局\"/>\n" +
                "    <meta name=\"keywords\" content=\"国家税务总局,山西省税务局,山西省,税务局,山西,税务\"/>\n" +
                "    <meta name=\"apple-mobile-web-app-title\" content=\"国家税务总局山西省税务局\">\n" +
                "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\"/>\n" +
                "    <meta name=\"format-detection\" content=\"telphone=no, email=no\"/>\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\"/>\n" +
                "\n" +
                "    <title>\n" +
                "        山西省医疗保障局 山西省财政厅 国家税务总局山西省税务局关于做好 2021年城乡居民基本医疗保障工作的通知\n" +
                "    </title>\n" +
                "    <!--网站名称-->\n" +
                "    <meta name=\"SiteName\" content=\"国家税务总局山西省税务局\">\n" +
                "\n" +
                "    <!--网站域名-->\n" +
                "    <meta name=\"SiteDomain\" content=\"shanxi.chinatax.gov.cn\"/>\n" +
                "\n" +
                "    <!--政府网站标识码-->\n" +
                "    <meta name=\"SiteIDCode\" content=\"bm29040001\"/>\n" +
                "\n" +
                "\n" +
                "    <!--栏目名称-->\n" +
                "    <meta name=\"ColumnName\" content=\"最新文件\"/>\n" +
                "    <!--栏目类别-->\n" +
                "    <meta name=\"ColumnType\" content=\"最新文件\"/>\n" +
                "\n" +
                "\n" +
                "    <!--标题-->\n" +
                "    <meta name=\"ArticleTitle\" content=\"山西省医疗保障局 山西省财政厅 国家税务总局山西省税务局关于做好 2021年城乡居民基本医疗保障工作的通知\"/>\n" +
                "\n" +
                "    <!--发布时间-->\n" +
                "    <meta name=\"PubDate\" content=\"2021-08-13\"/>\n" +
                "\n" +
                "    <!--来源-->\n" +
                "\n" +
                "    <meta name=\"ContentSource\" content=\"国家税务总局山西省税务局办公室\"/>\n" +
                "\n" +
                "\n" +
                "    <!--来源-->\n" +
                "    <link type='text/css' rel='stylesheet' href='/static/site/css/audioplayer.css?t=0.4477677878248808'/>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        var ctx = \"\";\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        var WWLJ = \"\";\n" +
                "        if (WWLJ != null && WWLJ != \"\") {\n" +
                "            window.location.href = WWLJ;\n" +
                "        }\n" +
                "    </script>\n" +
                "    <!--[if lt IE 9]>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/html5shiv@3.7.3/dist/html5shiv.min.js\"></script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/respond.js@1.4.2/dest/respond.min.js\"></script>\n" +
                "    <![endif]-->\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<!-- 头部组件-->\n" +
                "\n" +
                "<!-- <link rel=\"stylesheet\" href=\"//at.alicdn.com/t/font_1322669_nih6ud92t7d.css\"> -->\n" +
                "<link type='text/css' rel='stylesheet' href='/static/site/iconfont/iconfont.css?t=0.18215301816649154'/>\n" +
                "<link type='text/css' rel='stylesheet'\n" +
                "      href='/static/site/plugins/elves-sass-1.0.0/stylesheet/elves.min.css?t=0.8891089532626364'/>\n" +
                "<link type='text/css' rel='stylesheet' href='/static/site/css/sxsswj.min.css?t=0.48725126978003175'/>\n" +
                "<link type='text/css' rel='stylesheet'\n" +
                "      href='/static/site/plugins/swiper/css/idangerous.swiper.css?t=0.6035612748521473'/>\n" +
                "<link type='text/css' rel='stylesheet'\n" +
                "      href='/static/site/plugins/mobileSelect/css/mobileSelect.css?t=0.45033094470200685'/>\n" +
                "<link type='text/css' rel='stylesheet' href='/static/plugin/myPagination/page.css?t=0.4545233448085778'/>\n" +
                "<style>\n" +
                "    @media (min-width: 768px) {\n" +
                "        #page-newList .col-right {\n" +
                "            position: relative;\n" +
                "            height: 721px;\n" +
                "        }\n" +
                "\n" +
                "        #page-newList .pagetion {\n" +
                "            position: absolute;\n" +
                "            bottom: 0;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @media (max-width: 768px) {\n" +
                "        div.zjgf {\n" +
                "            font-size: .14rem\n" +
                "        }\n" +
                "\n" +
                "        div.zjgf a {\n" +
                "            min-width: .28rem;\n" +
                "            height: .28rem;\n" +
                "            line-height: .28rem;\n" +
                "        }\n" +
                "\n" +
                "        div.zjgf span.disabled {\n" +
                "            width: .6rem;\n" +
                "            height: .28rem;\n" +
                "            line-height: .28rem;\n" +
                "        }\n" +
                "\n" +
                "        div.zjgf span.current {\n" +
                "            min-width: .28rem;\n" +
                "            height: .28rem;\n" +
                "            line-height: .28rem;\n" +
                "        }\n" +
                "\n" +
                "        div.zjgf span#to_page {\n" +
                "            min-width: 1.08rem;\n" +
                "            height: .28rem;\n" +
                "            line-height: .28rem;\n" +
                "        }\n" +
                "\n" +
                "        div.zjgf button#to_btn {\n" +
                "            width: .66rem;\n" +
                "            height: .28rem;\n" +
                "            line-height: .28rem;\n" +
                "            color: #1a56a8;\n" +
                "        }\n" +
                "    }\n" +
                "</style>\n" +
                "\n" +
                "<header class=\"comp-header\">\n" +
                "    <section class=\"comp-header-links hidden-xs\">\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"col-xs-10\">\n" +
                "                <a href=\"javascript:;\" onclick=\"zh_tran('s')\" class=\"active\" id=\"zh_click_s\">简体</a>\n" +
                "                <a href=\"javascript:;\" onclick=\"zh_tran('t')\" id=\"zh_click_t\">繁体</a>\n" +
                "                <a href=\"javascript:void(0)\" id=\"cniil_wza\">无障碍阅读</a>\n" +
                "            </div>\n" +
                "            <div class=\"col-xs-10 text-right\">\n" +
                "                <a href=\"http://www.chinatax.gov.cn/\" target=\"_blank\" class=\"out_href_warn\">国家税务总局</a>\n" +
                "                <a href=\"http://www.shanxi.gov.cn/\" target=\"_blank\" class=\"out_href_warn\">山西省人民政府</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </section>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"col-xs-17 col-sm-10 col-md-10 col-lg-10\">\n" +
                "                <a class=\"navbar-brand\" href=\"/web\">\n" +
                "                    <img src=\"/static/site/images/logo.png\" alt=\"\" class=\"img-responsive\">\n" +
                "                </a>\n" +
                "            </div>\n" +
                "            <div class=\"col-xs-3 visible-xs-block\">\n" +
                "                <a href=\"javascript:;\" class=\"collapsed iconfont iconcaidan\" id='menu' data-toggle=\"collapse\"\n" +
                "                   data-target=\"#collapse-1\"></a>\n" +
                "            </div>\n" +
                "            <div class=\"pull-right hidden-xs funs-header\">\n" +
                "                <div class=\"type-links\">\n" +
                "                    <!-- <a href=\"\"><span class=\"iconfont iconkehuduan\" style=\"color: #1092dd\"></span><span>客户端</span></a> -->\n" +
                "                    <a href=\"/web/detail/sx-11400-2542-145951\" target=\"_blank\"><span class=\"iconfont iconweibo1\"\n" +
                "                                                                                     style=\"color: #e93f43\"></span><span>微博</span></a>\n" +
                "                    <a href=\"/web/detail/sx-11400-2542-145952\" target=\"_blank\"><span class=\"iconfont iconweixin\"\n" +
                "                                                                                     style=\"color: #03be13\"></span><span>微信</span></a>\n" +
                "                    <!-- <a href=\"\"><span class=\"iconfont iconyouxiang\" style=\"color: #ffa532\"></span><span>邮箱</span></a> -->\n" +
                "                </div>\n" +
                "\n" +
                "                <form id=\"searchForm\" action=\"/web/search/sx-11400\" method=\"post\">\n" +
                "                    <div class=\"search\">\n" +
                "                        <input type=\"text\" placeholder=\"请输入关键字\" id=\"keywords\" name=\"keywords\">\n" +
                "                        <button class=\"btn btn-info\" onclick=\"doSearch();\" type=\"button\">\n" +
                "                            <i class=\"iconfont iconfangdajing\"></i>\n" +
                "                            搜索\n" +
                "                        </button>\n" +
                "                    </div>\n" +
                "                </form>\n" +
                "\n" +
                "                <div class=\"hot-links\">\n" +
                "                    <span>本站热词：</span>\n" +
                "\n" +
                "                    <a href=\"javascript:;;\" onclick=\"doSearchKey('减税降费')\">减税降费</a>\n" +
                "\n" +
                "                    <a href=\"javascript:;;\" onclick=\"doSearchKey('电子税务局')\">电子税务局</a>\n" +
                "\n" +
                "                    <a href=\"javascript:;;\" onclick=\"doSearchKey('社保')\">社保</a>\n" +
                "\n" +
                "                    <a href=\"javascript:;;\" onclick=\"doSearchKey('发票')\">发票</a>\n" +
                "\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</header>\n" +
                "<!-- 导航组件-->\n" +
                "<nav class=\"navbar\">\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"collapse-1\">\n" +
                "            <ul class=\"nav navbar-nav\">\n" +
                "                <li><a href=\"/web\">首页</a></li>\n" +
                "                <li><a href=\"/xxgk\">信息公开</a></li>\n" +
                "                <li><a href=\"/xwdt\">新闻动态</a></li>\n" +
                "                <li class=\"active\"><a href=\"/zcwj\">政策文件</a></li>\n" +
                "                <li><a href=\"/nsfw\">纳税服务</a></li>\n" +
                "                <li><a href=\"/hdjl\">互动交流</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav>\n" +
                "\n" +
                "\n" +
                "<article id=\"page-newContent\">\n" +
                "\n" +
                "\n" +
                "    <ol class=\"breadcrumb\">\n" +
                "        <li><a href=\"/web\">首页</a></li>\n" +
                "\n" +
                "        <li><a href=\"/zcwj\">政策文件</a></li>\n" +
                "        <!--二级栏目  -->\n" +
                "\n" +
                "        <li class=\"active\"><a href=\"/web/list/sx-11400-545\">最新文件</a></li>\n" +
                "\n" +
                "    </ol>\n" +
                "    <!-- 手机返回上级 -->\n" +
                "    <div class=\"comp-nav\">\n" +
                "\n" +
                "        <a href=\"/web/list/sx-11400-545\" class=\"pull-left\"><i class=\"iconfont iconjiantou2\"></i></a>\n" +
                "\n" +
                "        <p>最新文件</p>\n" +
                "    </div>\n" +
                "\n" +
                "    <section class=\"container\">\n" +
                "        <!--\n" +
                "\n" +
                "                #has-right 控制页面左右布局方式\n" +
                "        -->\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"tb\" style=\"display: none\">\n" +
                "                <div class=\"col-xs-20 col-md-10\">\n" +
                "                    <span>索引号</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-10\">\n" +
                "                    <span>主题分类</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-20\">\n" +
                "                    <span>发文机关</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-20\">\n" +
                "                    <span>标题</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-10\">\n" +
                "                    <span>发文字号</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-10\">\n" +
                "                    <span>发布时间</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-20 col-md-20\">\n" +
                "                    <span>废止日期</span>\n" +
                "                    <p></p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"col-xs-20 col-left\">\n" +
                "                <div class=\"content\">\n" +
                "\n" +
                "                    <h1 id=\"main_title\">山西省医疗保障局 山西省财政厅 国家税务总局山西省税务局<br/>关于做好 2021年城乡居民基本医疗保障工作的通知</h1>\n" +
                "\n" +
                "\n" +
                "                    <div class=\"clearfix\">\n" +
                "                        <p>\n" +
                "\n" +
                "                            <span>发布时间：2021-08-13</span>\n" +
                "\n" +
                "                            <span>来源：国家税务总局山西省税务局办公室</span>\n" +
                "\n" +
                "                        </p>\n" +
                "                        <p>\n" +
                "                            <span class=\"hidden-xs\">字号：<a href=\"javascript:;\" onclick=\"setFontSize('18px');\">[ 大 ]</a><a\n" +
                "                                    href=\"javascript:;\" onclick=\"setFontSize('16px');\">[ 中 ]</a><a href=\"javascript:;\"\n" +
                "                                                                                                   onclick=\"setFontSize('14px');\">[ 小 ]</a></span>\n" +
                "                            <a class=\"hidden-xs\" href=\"javascript:;\" onclick=\"print();\">打印本页</a>\n" +
                "                            <a class=\"hidden-xs\" href=\"javascript:;\" id=\"zwxz\">下载</a>\n" +
                "                            <span class=\"hidden-xs bdsharebuttonbox\">\n" +
                "                                    <i class=\"iconfont iconfenxiang bd-r\" style=\"color: #999;font-size:20px\"></i>\n" +
                "                                <!-- <a href=\"#\" style=\"color: #999;font-size:20px\" data-cmd=\"more\" class=\"iconfont iconfenxiang bd-r\"></a> -->\n" +
                "                                    <a href=\"#\" onclick=\"_trackData.push(['addaction','文章页分享', '微博'])\" data-cmd=\"tsina\"\n" +
                "                                       title=\"分享到新浪微博\" class=\"iconfont iconweibo1 bd-r\" style=\"color: #e93f43\"></a>\n" +
                "                                    <a href=\"#\" onclick=\"_trackData.push(['addaction','文章页分享', '微信'])\" data-cmd=\"weixin\"\n" +
                "                                       title=\"分享到微信\" class=\"iconfont iconweixin bd-r\" style=\"color: #03be13\"></a>\n" +
                "                                    <a href=\"#\" onclick=\"_trackData.push(['addaction','文章页分享', 'QQ空间'])\"\n" +
                "                                       data-cmd=\"qzone\" title=\"分享到QQ空间\" class=\"iconfont iconqqkongjian bd-r\"\n" +
                "                                       style=\"color: #ffa532\"></a>\n" +
                "                                </span>\n" +
                "                            <script>window._bd_share_config = {\n" +
                "                                \"common\": {\n" +
                "                                    \"bdSnsKey\": {},\n" +
                "                                    \"bdText\": \"\",\n" +
                "                                    \"bdMini\": \"2\",\n" +
                "                                    \"bdMiniList\": false,\n" +
                "                                    \"bdPic\": \"\",\n" +
                "                                    \"bdStyle\": \"0\",\n" +
                "                                    \"bdSize\": \"24\"\n" +
                "                                }, \"share\": {}/* ,\"image\":{\"viewList\":[\"qzone\",\"tsina\",\"tqq\",\"renren\",\"weixin\"],\"viewText\":\"分享到：\",\"viewSize\":\"16\"} *//* ,\"selectShare\":{\"bdContainerClass\":null,\"bdSelectMiniList\":[\"qzone\",\"tsina\",\"tqq\",\"renren\",\"weixin\"]} */\n" +
                "                            };\n" +
                "                            with (document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];</script>\n" +
                "                        </p>\n" +
                "                    </div>\n" +
                "\n" +
                "                    <section id=\"main\">\n" +
                "                        <!-- 最新文件填加 成文日期和文号 添加人 冯江伟 -->\n" +
                "\n" +
                "                        <p style=\"color:#910A08;margin:-10px 0 50px;\" class='text-des'>\n" +
                "\n" +
                "                            【文&ensp;&ensp;&ensp;&ensp;号】 晋医保发〔2021〕14号<br>\n" +
                "\n" +
                "                            【成文日期】 2021-07-28<br>\n" +
                "\n" +
                "                            【是否有效】 全文有效<br>\n" +
                "\n" +
                "\n" +
                "                        </p>\n" +
                "\n" +
                "                        <!-- 最新文件填加 成文日期和文号结束  -->\n" +
                "                        <div id=\"zoom\">\n" +
                "                            <p>\n" +
                "                            </p>\n" +
                "                            <p>\n" +
                "                                　　为贯彻落实2021年《政府工作报告》和《山西省委山西省人民政府关于深化医疗保璋制度改革的意见》决策部署完善统一的城乡居民基本医疗保险制度和大病保险制度，按照国家医保局、财政部、国家稅务总局《关于做好2021年城乡居民基本医疗保障工作的通知》（医保发〔2020〕32)号要求，现就做好2021年我省城乡居民医疗保障工作通知如下：<br\n" +
                "                                    style=\"-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box;\"/></p>\n" +
                "                            <p><strong>　　一、继续提高居民医保筹资标准</strong></p>\n" +
                "                            <p>\n" +
                "                                　　2021年，全省城乡居民基本医疗保险（以下简称“居民医保”）人均财政补助标准提高30元，达到580元。按中央和我省财政补助政策，一般县中央财政负担60%,省级和市县两级财政各负担20%;享受西部政策的县中央财政负担80%,省级和市县两级财政各负担10%,市县两级分担比例自行确定。各级财政补助标准为：一般县中央补助348元、省级补助116元、市县两级补助不低于116元；享受西部政策的县中央补助464元、省级补助58元、市县两级补助不低于58元。市县财政部门要按本通知要求足额安排财政补助资金，原则上应于8月10日前全部拨付到位。进一步放开参加基本医疗保险的户籍限制，对持居住证参保的当地居民医保，各级财政要按当地居民相同标准给予补助。2021年预收2022年度的个人缴费标准同步提高40元，达到每人每年320元。</p>\n" +
                "                            <p>　　各市要根据城乡居民大病保险（以下简称“大病保险”）基金运行情况，在确保现有筹资水平不降低的基础上，统筹考虑确定大病保险筹资标准。</p>\n" +
                "                            <p><strong>　　二、巩固完善居民医保待遇水平</strong></p>\n" +
                "                            <p>\n" +
                "                                　　各市要做好医疗保障待遇淸单落地工作,坚决树立淸单意识和科学决策意识,严格执行基本医疗保障支付范围和标准。要加强基本医保、大病保险和医疗救助三重保障制度衔接，充分发挥综合保障功能，进一步巩固稳定住院待遇保障水平，政策范围内基金支付比例稳定在75%左右。全面落实全省统一的普通门诊统筹政策和门诊慢性病病种及准入标准，并做好待遇衔接。全面实施高血压、糖尿病门诊用药保障和健康管理专项行动计划，确定太原市为专项行动示范城市，带动全省深化“两病”门诊用药保障机制。</p>\n" +
                "                            <p>\n" +
                "                                　　加快健全重大疾病医疗保险和救助制度,大病保险继续实施对特困人员（含孤儿和事实无人抚养儿童，下同)、低保对象和返贫致贫人口倾斜支付政策。完善统一规范的医疗救助制度，分类资助特困人员、低保对象和返贫致贫人口等困难群众参加居民医保，根据实际合理确定救助待遇标准,夯实医疗救助托底保障功能。</p>\n" +
                "                            <p>\n" +
                "                                　　要规范待遇享受等待期（以下简称“等待期”）设置,对居民医保在集中参保期内参保的、在职工医保中断缴费3个月内参加居民医保的，以及新生儿、农村低收入人口等特殊群体，不设等待期。</p>\n" +
                "                            <p><strong>　　三、巩固拓展医疗保障脱贫攻坚成果有效衔接乡村振兴战略</strong></p>\n" +
                "                            <p>\n" +
                "                                　　进一步巩固拓展医疔保障脱贫攻坚成果，按照省委、省政府的统一部署，优化调整医疗保障帮扶政策，逐步实现由集中资源支持脱贫攻坚向统等基本医保、大病保险、医疗救助三重制度常态化保障平稳过渡。完善三重制度综合保障政策，分类落实好脱贫人口各类医疗保障待遇，过渡期内持续抓好过度保障治理，清理存量过度保障政策。</p>\n" +
                "                            <p>\n" +
                "                                　　建立防范化解因病返贫致贫长效机制，做好高额费用负担患者因病返贫致贫风险监测，及时将符合条件的人员纳入医疗救助范围，依申请落实医疗救助政策。要统筹完善托底保障措施，加大门诊慢性病、特殊疾病救助保障，对规范转诊且在省域内就医的救助对象经三重制度保障后政策范围内个人负担仍然较重的，探索给予倾斜救助。</p>\n" +
                "                            <p><strong>　　四、加强医保支付管理</strong></p>\n" +
                "                            <p>\n" +
                "                                　　根据《医疗机构医疗保障定点管理暂行办法》和《零售药店医疗保障定点管理暂行办法》规定，及时出台本省定点医药机构准入细则，进一步简化、优化医药机构医保定点准入工作，及时将符合条件的医药机构纳入医保定点范围。着力推进医保支付方式改革，推动8个DRG付费试点统筹地区和4个DIP试点统筹地区逐步实现实际付费。积极探索点数法与统筹地区医保基金总额预算相结合，逐步使用区域医保基金总额控制代替具体医疗机构总额控制。完善与门诊共济保障相适应的付費机制。加强医保目录管理，严格落实《基本医疗保险用药管理暂行办法》,严格执行《国家基本医疗保险、工伤保险和生育保险药品目录（2020年）》，贯彻落实《关于建立完善国家医保谈判药品“双通道”管理机制的指导意见》（医保发〔2021〕28号），健全谈判药品落地监测机制，制定出台我省贯彻落实意见，完善基本医保医用耗材和医疗服务项目管理。</p>\n" +
                "                            <p><strong>　　五、加强药品耗材集中带量采购和价格管理</strong></p>\n" +
                "                            <p>\n" +
                "                                　　全面贯彻落实《国务院办公厅关于推动药品集中带量采购工作常态化制度化开展的意见》（国办发〔2021〕2号）和《山西省人民政府办公厅关于推动药品集中带量采购工作常态化制度化开展和医用耗材分类集中采购的实施意见》（晋政办发〔2021〕45号），做好国家组织药品和医用耗材集中带量采购中选结果的落地实施工作，积极组织开展省级、省际联盟药品耗材集中带量采购工作，同步落实医保基金预付、支付标准协同、结余留用等配套政策措施，做好采购协议期满后的接续工作，推动药品集中带量采购工作常态化制度化开展，积极探索推进医用耗材分类集中釆购，引导药品耗材价格回归合理水平，有效减轻群众医药费用负担。</p>\n" +
                "                            <p>\n" +
                "                                　　推进实施医药价格和招采信用评价制度，医药企业参加或委托参加我省药品和医用耗材集中带量采购、挂网采购、备案釆购须提交守信承诺。对拒绝提交守信承诺的投标挂网企业采取约束措施，推动信用评价制度落地见效。不断完善医疗服务项目价格审核机制，加快审核新增医疗服务项目价格。积极开展公立医院医疗服务项目价格评估，根据评估结果开展医疗服务价格动态调整，持续优化医疗服务价格结构。</p>\n" +
                "                            <p><strong>　　六、加强基金监督管理</strong></p>\n" +
                "                            <p>\n" +
                "                                　　开展《医疗保障基金使用监督管理条例》宣传培训，完善医疗保障基金使用监管制度，推进基金监管制度体系改革。持续开展全覆盖监督检查和对定点医药机构的抽查复查，聚焦“假病人、假病情、假票据”欺诈骗保问题和严重违法违规行为，实施重点整治，推进专项行动和日常监管向纵深发展。健全完善举报奖励机制，加大媒体曝光力度，营造维护基金安全的良好氛围。加强基金运行预警分析和监管，确保基金收支平衡和累计结余在安全线以上。</p>\n" +
                "                            <p>\n" +
                "                                　　在夯实市级统筹基础上，积极稳妥推动基本医保省级统筹。在统一执行城乡居民普通门诊统筹、门诊慢性病病种及准入标准的基础上，完善职工门诊共济保障机制，逐步统一职工普通门诊统筹和职工门诊大额疾病政策。逐步推进医疗救助管理层次与基本医保统筹层次相协调。</p>\n" +
                "                            <p>\n" +
                "                                　　結合新冠肺炎疫情影响，加强基金收支运行分析，开展基金使用绩效评价，完善收支预算管理，健全风险预警、评估化、解机制及预案。探索综合人口老龄化、慢性病等疾病普变化、医疗支出水平增长等因素，开展基金支出预测分析。</p>\n" +
                "                            <p><strong>　　七、加强医保公共管理服务</strong></p>\n" +
                "                            <p>\n" +
                "                                　　继续做好新冠肺炎患者医疗费用结算和跨省就医医保费用全国清算工作，及时结算新冠疫苗及接种费用。全面落实《全国医疗保障经办政务服务事项淸单》，推动医保公共服务标准化规范化建设C推进医保经办标准化窗口和服务示范点建设。增强基层医疗保障公共服务能力，推进医疗保障公共服务纳入县乡村公共服务一体化建设，在医保经办力量配置不足的地区，可通过政府购买服务等方式，加强医疗保障经办力量。规范商业保险机构承办大病保险的管理服务。推进医保经办管理服务与网上政务服务平台等有效衔接，坚持传统服务方式与智能服务方式创新并行，提高线上服务适老化水平，优化线下服务模式，保障老年人、重度残疾人等特殊人群顺畅便捷办理业务。</p>\n" +
                "                            <p>\n" +
                "                                　　适应平台经济、共享经济等新经济、新业态发展，完善新就业形态从业人员参保缴费方式。推动公安、民政、人力资源社会保障、卫生健康、市场监管、税务、教育、残联、乡村振兴等部门的数据共享交换机制，加强人员信息比对和共享，核实断保、停保人员情况，实现参保信息动态更新。协同税务部门优化社会保险费征收系统，防止同一参保人在同一时间段内重复缴纳医保费。强化全面参保计划中市县党委、政府责任，压实乡镇街道参保征缴责任，增强全民参保意识，确保应保尽保。坚持线上与线下结合，推进参保人员办理参保登记、申报缴费、查询信息、欠费提醒等“一次不用跑”。加快推进高频医保服务事项跨省通办。</p>\n" +
                "                            <p>\n" +
                "                                　　优化普通门诊费用跨省直接结算服务，探索门诊慢性病、特殊疾病费用跨省直接结算实现路径。加快建设全国统一的医疗保障信息平台，优化完善运维服务管理体系、安全管理体系、制度规范以及平台功能-加强医保数据安全管理和信息共享，加快医保信息业务标准编码落地应用。</p>\n" +
                "                            <p><strong>　　八、做好组织实施</strong></p>\n" +
                "                            <p>\n" +
                "                                　　要高度重视城乡居民医疗保障工作，切实加强组织保障，压实工作责任，确保各项政策措施落地见效。要强化服务意识，优化服务方式，更好为人民群众提供公平可及、便捷高效、温暖舒心的医疗保障服务。要进一步加大政策宣传力度，普及医疗保险互助共济、责任共担、共建共享的理念,增强群众参保缴费意识，合理引导社会预期。各级医疗保障、财政和税务部门要加强统筹协调，建立健全部门信息沟通和工作协同机制，做好基金运行评估和风险监测，制定工作预案，遇到重大情况要及时按要求报告。<br\n" +
                "                                    style=\"-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box;\"/></p>\n" +
                "                            <p><br style=\"-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box;\"/></p>\n" +
                "                            <p style=\"text-align: right;\">山西省医疗保障局 山西省财政厅</p>\n" +
                "                            <p style=\"text-align: right;\">国家税务总局山西省税务局</p>\n" +
                "                            <p style=\"text-align: right;\">2021年7月28日</p>\n" +
                "                            <p>\n" +
                "\n" +
                "\n" +
                "                            </p>\n" +
                "                        </div>\n" +
                "                        <div class=\"ewm\">\n" +
                "                            <div id=\"QRCode\"></div>\n" +
                "                            <img src=\"/static/site/images/myLogo.png\" style=\"display: none\" id=\"myLogo\">\n" +
                "                            <p>扫一扫在手机打开当前页</p>\n" +
                "                        </div>\n" +
                "                    </section>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "\n" +
                "            <div class=\"col-xs-20 col-right\">\n" +
                "\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </section>\n" +
                "\n" +
                "\n" +
                "</article>\n" +
                "<div id=\"video0\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video1\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video2\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video3\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video4\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video5\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video6\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video7\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video8\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video9\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video10\" style=\"margin: auto\"></div>\n" +
                "<div id=\"video11\" style=\"margin: auto\"></div>\n" +
                "<!-- 底部部组件-->\n" +
                "\n" +
                "\n" +
                "<footer class=\"footer\">\n" +
                "    <div class=\"footer-main\">\n" +
                "        <div class=\"clearfix\">\n" +
                "            <div class=\"footer-img-links hidden-xs\">\n" +
                "                <a href=\"http://bszs.conac.cn/sitename?method=show&id=07CED2186E764F87E053012819AC2D23\" target=\"_blank\"><img\n" +
                "                        src=\"/static/site/images/jg.png\" alt=\"\"></a>\n" +
                "                <a href=\"javascript:;\">\n" +
                "                    <script id=\"_jiucuo_\" sitecode='bm29040001'\n" +
                "                            src='https://zfwzgl.www.gov.cn/exposure/jiucuo.js'></script>\n" +
                "                </a>\n" +
                "            </div>\n" +
                "            <div class=\"footer-text\">\n" +
                "                <p class='footer-text-about hidden-xs'>\n" +
                "\n" +
                "                    <a href=\"/web/wzdt/sx-11400\">网站地图</a><i></i>\n" +
                "                    <a href=\"/web/list/sx-11400-1373\">网站管理</a><i></i>\n" +
                "                    <a target=\"_blank\" href=\"/web/detail/sx-11400-1367-144932\">联系我们</a>\n" +
                "\n" +
                "                </p>\n" +
                "                <P class='footer-text-unit'>\n" +
                "                    <span>主办单位：国家税务总局山西省税务局</span>\n" +
                "                    <span>地址：太原市水西门街31号</span>\n" +
                "                    <span>纳税缴费服务热线：0351—12366</span>\n" +
                "                </P>\n" +
                "                <P class=\"footer-text-copyright hidden-xs\">\n" +
                "                    <span>网站标识码：bm29040001</span>\n" +
                "                    <span onclick=\"window.open('https://beian.miit.gov.cn')\">晋ICP备08101611号-2</span>\n" +
                "                    <span onclick=\"window.open('http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=14010602060578')\">晋公网安备 14010602060578号</span>\n" +
                "                </P>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</footer>\n" +
                "<script defer async type=\"text/javascript\" src=\"http://api.govwza.cn/cniil/assist.js?sid=82430&pos=left\"></script>\n" +
                "<!-- 无障碍 -->\n" +
                "<script type='text/javascript' src='/static/site/plugins/jquery.min.js?t=0.8739488703245053'></script>\n" +
                "<script type='text/javascript'\n" +
                "        src='/static/site/plugins/elves-sass-1.0.0/javascript/elves.min.js?t=0.05844960869624716'></script>\n" +
                "<script type='text/javascript'\n" +
                "        src='/static/site/plugins/swiper/js/idangerous.swiper.min.js?t=0.3879721726590364'></script>\n" +
                "<script type='text/javascript' src='/static/site/plugins/jquery.SuperSlide.js?t=0.5158777260937609'></script>\n" +
                "<script type='text/javascript'\n" +
                "        src='/static/site/plugins/mobileSelect/js/mobileSelect.min.js?t=0.6044735111855519'></script>\n" +
                "<script type='text/javascript' src='/static/site/plugins/date.js?t=0.7206458674831828'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/jquery-qrcode.js?t=0.1168891601205786'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/jquery-qrcode.min.js?t=0.3488401139198082'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/zh_hl.js?t=0.44968266838365034'></script>\n" +
                "<script type='text/javascript'\n" +
                "        src='/static/plugin/myPagination/jquery.myPagination6.0.1.js?t=0.16351737191431526'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/common.js?t=0.5177478767723765'></script>\n" +
                "<script type='text/javascript' src='/static/plugin/ckplayer/ckplayer.js?t=0.49355737173771375'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/audioplayer.js?t=0.6085699735480353'></script>\n" +
                "\n" +
                "\n" +
                "<script>\n" +
                "    var _hmt = _hmt || [];\n" +
                "    (function () {\n" +
                "        var hm = document.createElement(\"script\");\n" +
                "        var domain = document.domain;\n" +
                "        if (domain == 'shanxi.chinatax.gov.cn') {\n" +
                "            hm.src = \"//hm.baidu.com/hm.js?27840e20f0a5257979c9cc9ef01aa9a9\";\n" +
                "        } else if (domain == 'www.sx-n-tax.gov.cn') {\n" +
                "            hm.src = \"//hm.baidu.com/hm.js?72b5a40ec1358230f730948944f0ce02\";\n" +
                "        } else {\n" +
                "            hm.src = \"//hm.baidu.com/hm.js?0d6a8a2ae7112ecf5e03cb525acf6891\";\n" +
                "        }\n" +
                "        var s = document.getElementsByTagName(\"script\")[0];\n" +
                "        s.parentNode.insertBefore(hm, s);\n" +
                "    })();\n" +
                "</script>\n" +
                "\n" +
                "<script language=\"JavaScript\">var _trackDataType = 'web';\n" +
                "var _trackData = _trackData || [];</script>\n" +
                "<script type=\"text/javascript\" charset=\"utf-8\" id=\"kpyfx_js_id_10002888\"\n" +
                "        src=\"//fxsjcj2.kaipuyun.cn/count/10002888/10002888.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "    var sdm = 'sx';\n" +
                "    var name = \"\";\n" +
                "    if (sdm == 'sx') {\n" +
                "        name = \"山西省\";\n" +
                "    } else if (sdm == 'ty') {\n" +
                "        name = \"太原市\";\n" +
                "    } else if (sdm == 'dt') {\n" +
                "        name = \"大同市\";\n" +
                "    } else if (sdm == 'xz') {\n" +
                "        name = \"忻州市\";\n" +
                "    } else if (sdm == 'sz') {\n" +
                "        name = \"朔州市\";\n" +
                "    } else if (sdm == 'yq') {\n" +
                "        name = \"阳泉市\";\n" +
                "    } else if (sdm == 'jc') {\n" +
                "        name = \"晋城市\";\n" +
                "    } else if (sdm == 'jz') {\n" +
                "        name = \"晋中市\";\n" +
                "    } else if (sdm == 'lf') {\n" +
                "        name = \"临汾市\";\n" +
                "    } else if (sdm == 'll') {\n" +
                "        name = \"吕梁市\";\n" +
                "    } else if (sdm == 'yc') {\n" +
                "        name = \"运城市\";\n" +
                "    } else if (sdm == 'cz') {\n" +
                "        name = \"长治市\";\n" +
                "    }\n" +
                "    var extLinkArr = [\"shanxi.chinatax.gov.cn\", \"202.99.207.241\", \"183.203.219.50\", \"localhost\", \"70.12.100.204\"];//排除包含这些字符的链接\n" +
                "    function _openExtLink(a) {\n" +
                "        if (a.href.indexOf(\"javascript:\") >= 0 || a == \"\" || a == null) {\n" +
                "            return false;\n" +
                "        }\n" +
                "        var r = a.href;\n" +
                "        for (var i = 0; i < extLinkArr.length; i++) {\n" +
                "            if (r.indexOf(extLinkArr[i]) > -1) {\n" +
                "                return true;//如果在指定的排除链接中，就执行href；\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        var r = confirm(\"您将离开国家税务总局\" + name + \"税务局网站，是否继续访问？\");\n" +
                "        if (r == true) {\n" +
                "            return true;\n" +
                "        } else {\n" +
                "            return false;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    $(function () {\n" +
                "\n" +
                "        $(\".out_href_warn\").click(function () {\n" +
                "            return _openExtLink(this);\n" +
                "        });\n" +
                "    });\n" +
                "\n" +
                "    function openURL(val) {\n" +
                "        var r = confirm(\"您将离开国家税务总局\" + name + \"税务局网站，是否继续访问？\");\n" +
                "        if (r == true) {\n" +
                "            window.open(val);\n" +
                "        } else {\n" +
                "            return false;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    $(function () {\n" +
                "\n" +
                "        $(\"#nav>ul>li\").mouseenter(function () {\n" +
                "            if ($(this).children('.nav-child')) {\n" +
                "                $(this).children('.nav-child').stop().slideDown()\n" +
                "            }\n" +
                "        }).mouseleave(function (e) {\n" +
                "\n" +
                "            if ($(this).children('.nav-child')) {\n" +
                "                $(this).children('.nav-child').stop().slideUp()\n" +
                "            }\n" +
                "        })\n" +
                "\n" +
                "    })\n" +
                "</script>\n" +
                "\n" +
                "<script type='text/javascript' src='/static/site/js/FileSaver.js?t=0.08186948894560908'></script>\n" +
                "<script type='text/javascript' src='/static/site/js/jquery.wordexport.js?t=0.6643125535252192'></script>\n" +
                "<script type=\"text/javascript\">$(\".m-slide\").slide({\n" +
                "    titCell: \".tab li\",\n" +
                "    mainCell: \".img\",\n" +
                "    effect: \"fold\",\n" +
                "    autoPlay: true\n" +
                "});</script>\n" +
                "\n" +
                "<script>\n" +
                "    //生成二维码\n" +
                "    $(\"#QRCode\").qrcode({\n" +
                "        render: \"canvas\", //也可以替换为table\n" +
                "        minVersion: 1,       // version range somewhere in 1 .. 40\n" +
                "        maxVersion: 40,\n" +
                "        ecLevel: 'H',        //识别度  'L', 'M', 'Q' or 'H'\n" +
                "        left: 0,\n" +
                "        top: 0,\n" +
                "        size: 130,           //尺寸\n" +
                "        fill: '#000',        //二维码颜色\n" +
                "        background: null,    //背景色\n" +
                "        text: document.location.href,     //二维码内容\n" +
                "        radius: 0.1,         // 0.0 .. 0.5\n" +
                "        quiet: 2,            //边距\n" +
                "        mode: 4,\n" +
                "        mSize: 0.2,          //图片大小\n" +
                "        mPosX: 0.5,\n" +
                "        mPosY: 0.5,\n" +
                "        label: 'jQuery.qrcode',\n" +
                "        fontname: 'sans',\n" +
                "        fontcolor: '#000',\n" +
                "        image: $('#myLogo')[0]\n" +
                "    });\n" +
                "\n" +
                "    function setFontSize(fontSize) {\n" +
                "        $(\"#main *\").css('fontSize', fontSize);\n" +
                "    }\n" +
                "\n" +
                "    pl();\n" +
                "\n" +
                "    function pl() {\n" +
                "        var embeds = $(\"#main\").find('embed');\n" +
                "        for (var i = 0; i < embeds.length; i++) {\n" +
                "            //判断是音频还是视频\n" +
                "            if (embeds.eq(i).attr('src').indexOf(\".mp3\") != -1) {//音频插件\n" +
                "                embeds.eq(i).replaceWith('<div style=\"margin:50px auto 15px;width:' + embeds.eq(i).attr(\"width\") + 'px\"><audio id=\"musicAudio' + i + '\" loop=\"\" src=\"' + embeds.eq(i).attr(\"src\") + '\"  preload controls style=\"width:' + embeds.eq(i).attr(\"width\") + 'px\"></audio></div>');\n" +
                "                $(\"#musicAudio\" + i).audioPlayer();\n" +
                "            } else {//视频插件\n" +
                "                var videoObject = {\n" +
                "                    container: '#video' + i,//“#”代表容器的ID，“.”或“”代表容器的class\n" +
                "                    variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象\n" +
                "                    flashplayer: false,//如果强制使用flashplayer则设置成true\n" +
                "                    poster: \"/static/plugin/CuPlayer/images/start.jpg\",\n" +
                "                    video: embeds.eq(i).attr('src')//视频地址\n" +
                "                };\n" +
                "                var player = new ckplayer(videoObject);\n" +
                "                $(\"#video\" + i).width(embeds.eq(i).attr(\"width\") + \"px\").height(embeds.eq(i).attr(\"height\") + \"px\");\n" +
                "                embeds.eq(i).replaceWith($(\"#video\" + i));\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "        var videos = $(\"#main\").find('video');\n" +
                "        for (var i = 0; i < videos.length; i++) {\n" +
                "            //判断是音频还是视频\n" +
                "            if (videos.eq(i).attr('src').indexOf(\".mp3\") != -1) {//音频插件\n" +
                "                videos.eq(i).replaceWith('<div style=\"margin:50px auto 15px;width:' + videos.eq(i).attr(\"width\") + 'px\"><audio id=\"musicAudio' + i + '\" loop=\"\" src=\"' + videos.eq(i).attr(\"src\") + '\"  preload controls style=\"width:' + videos.eq(i).attr(\"width\") + 'px\"></audio></div>');\n" +
                "                $(\"#musicAudio\" + i).audioPlayer();\n" +
                "            } else {//视频插件\n" +
                "                var videoObject = {\n" +
                "                    container: '#video' + i,//“#”代表容器的ID，“.”或“”代表容器的class\n" +
                "                    variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象\n" +
                "                    flashplayer: false,//如果强制使用flashplayer则设置成true\n" +
                "                    poster: \"/static/plugin/CuPlayer/images/start.jpg\",\n" +
                "                    video: videos.eq(i).attr('src')//视频地址\n" +
                "                };\n" +
                "                var player = new ckplayer(videoObject);\n" +
                "                $(\"#video\" + i).width(videos.eq(i).attr(\"width\") + \"px\").height(videos.eq(i).attr(\"height\") + \"px\");\n" +
                "                videos.eq(i).replaceWith($(\"#video\" + i));\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    /*文章下载*/\n" +
                "    $(\"#zwxz\").click(function (event) {\n" +
                "        $(\"#zoom\").wordExport(document.getElementById(\"main_title\").innerText);\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>");
        System.out.println(s1);
        }}