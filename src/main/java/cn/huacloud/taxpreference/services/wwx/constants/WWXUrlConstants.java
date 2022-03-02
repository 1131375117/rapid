package cn.huacloud.taxpreference.services.wwx.constants;

/**
 * @author wangkh
 */
public interface WWXUrlConstants {

    String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/";

    //服务商相关
    String serviceUrl = baseUrl + "service/";
    String suiteTokenUrl = serviceUrl + "get_suite_token";
    String preAuthCodeUrl = serviceUrl + "get_pre_auth_code?suite_access_token=%s";
    String permanentCodeUrl = serviceUrl + "get_permanent_code?suite_access_token=%s";
    String sessionInfoUrl = serviceUrl + "set_session_info?suite_access_token=%s";
    String installUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=%s&pre_auth_code=%s&redirect_uri=%s&state=STATE";

    String providerTokenUlr = serviceUrl + "get_provider_token";
    String registerCodeUrl = serviceUrl + "get_register_code?provider_access_token=%s";
    String registerUrl = "https://open.work.weixin.qq.com/3rdservice/wework/register?register_code=%s";

    String ssoAuthUrl = "https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=%s&redirect_uri=%s&state=%s&usertype=%s";
    String loginInfoUrl = serviceUrl + "get_login_info?access_token=%s";

    //通讯录转译
    String contactUploadUrl = serviceUrl + "media/upload?provider_access_token=%s&type=%s";
    String contactTransUrl = serviceUrl + "contact/id_translate?provider_access_token=%s";
    String transResultUrl = serviceUrl + "batch/getresult?provider_access_token=%s&jobid=%s";

    //公司相关
    String corpTokenUrl = serviceUrl + "get_corp_token?suite_access_token=%s";
    String departmentUrl = baseUrl + "department/list?access_token=%s";
    String userSimplelistUrl = baseUrl + "user/simplelist?access_token=%s&department_id=%s&fetch_child=%s";
    String userDetailUrl = baseUrl + "user/get?access_token={access_token}&userid={user_id}";

    //外部联系人

    //获取配置了客户联系功能的成员列表 https://work.weixin.qq.com/api/doc/90001/90143/92576
    String extContactFollowUserListUrl = baseUrl + "externalcontact/get_follow_user_list?access_token=%s";
    //获取客户列表  https://work.weixin.qq.com/api/doc/90001/90143/92264
    String extContactListUrl = baseUrl + "externalcontact/list?access_token=%s&userid=%s";
    //获取客户群列表 https://work.weixin.qq.com/api/doc/90001/90143/93414
    String extContactGroupchatUrl = baseUrl + "externalcontact/groupchat/list?access_token=%s";

    //消息推送
    String messageSendUrl = baseUrl + "message/send?access_token=%s";

    //素材管理
    //https://open.work.weixin.qq.com/api/doc/90001/90143/90389
    //type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
    String mediaUploadUrl = baseUrl + "media/upload?access_token=%s&type=%s";
    String mediaUploadimgUrl = baseUrl + "media/uploadimg?access_token=%s";
    String mediaGetUrl = baseUrl + "media/get?access_token=%s&media_id=%s";
    String mediaGetJssdkUrl = baseUrl + "media/get/jssdk?access_token=%s&media_id=%s";

    //审批
    //审批应用 https://work.weixin.qq.com/api/doc/90001/90143/91956
    String oaCopyTemplateUrl = "oa/approval/copytemplate?access_token=%s";
    String oaGetTemplateUrl = "/oa/gettemplatedetail?access_token=%s";
    String oaApplyEventUrl = "oa/applyevent?access_token=%s";
    String oaGetApprovalUrl = "oa/getapprovaldetail?access_token=%s";

    //审批流程引擎 https://work.weixin.qq.com/api/doc/90001/90143/93798
    String openApprovalDataUrl = baseUrl + "corp/getopenapprovaldata?access_token=ACCESS_TOKEN";


    // H5应用
    //scope应用授权作用域。
    //snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
    //snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
    //snsapi_info：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
    //https://work.weixin.qq.com/api/doc/90001/90143/91120
    String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    //https://work.weixin.qq.com/api/doc/90001/90143/91121
    String oauthUserUrl = serviceUrl + "getuserinfo3rd?suite_access_token=%s&code=%s";
    //https://work.weixin.qq.com/api/doc/90001/90143/91122
    String oauthUserDetailUrl = serviceUrl + "getuserdetail3rd?suite_access_token=%s";
    //https://work.weixin.qq.com/api/doc/90001/90144/90539
    String jsapiTicketUrl = baseUrl + "get_jsapi_ticket?access_token=%s";
    //https://work.weixin.qq.com/api/doc/90001/90144/90539#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket
    String jsapiTicketAgentUrl = baseUrl + "ticket/get?access_token=%s&type=agent_config";

    //家校沟通
    //https://open.work.weixin.qq.com/api/doc/90001/90143/92291
    String extContactMessageSendUrl = baseUrl + "externalcontact/message/send?access_token=%s";

    //此oauth与H5oauth一致  https://work.weixin.qq.com/api/doc/90001/90143/91861
    String schoolOauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    //https://work.weixin.qq.com/api/doc/90001/90143/91711
    String schoolOauthUserUrl = serviceUrl + "getuserinfo3rd?suite_access_token=%s&code=%s";

    String schoolUrl = baseUrl + "school/";
    //https://work.weixin.qq.com/api/doc/90001/90143/92038
    String schoolUserGetUrl = schoolUrl + "user/get?access_token=%s&userid=%s";
    //https://work.weixin.qq.com/api/doc/90001/90143/92299
    String schoolDepartmentListUrl = schoolUrl + "department/list?access_token=%s&id=%s";
    //https://work.weixin.qq.com/api/doc/90001/90143/92043
    String schoolUserListUrl = schoolUrl + "user/list?access_token=%s&department_id=%s&fetch_child=%s";


    //小程序应用
    //小程序登录流程 https://work.weixin.qq.com/api/doc/90001/90144/92427
    //code2Session https://work.weixin.qq.com/api/doc/90001/90144/92423
    String code2sessionUrl = serviceUrl + "miniprogram/jscode2session?suite_access_token=%s&js_code=%s&grant_type=authorization_code";
}
