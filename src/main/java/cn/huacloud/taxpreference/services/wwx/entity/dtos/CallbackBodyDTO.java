package cn.huacloud.taxpreference.services.wwx.entity.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangkh
 */
@Data
@XmlRootElement(name = "xml")
public class CallbackBodyDTO {
    /**
     * 企业微信的CorpID，当为第三方应用回调事件时，CorpID的内容为suiteid
     */
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    /**
     * 	接收的应用id，可在应用的设置页面获取。仅应用相关的回调会带该字段。
     */
    @JacksonXmlProperty(localName = "AgentID")
    private String agentID;
    /**
     * 消息结构体加密后的字符串
     */
    @JacksonXmlProperty(localName = "Encrypt")
    private String encrypt;
}
