package cn.huacloud.taxpreference.services.wwx.entity.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangkh
 */
@Data
@XmlRootElement(name = "xml")
public class CallBackBodyDTO {
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "AgentID")
    private String agentID;
    @JacksonXmlProperty(localName = "Encrypt")
    private String encrypt;
}
