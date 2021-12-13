package cn.huacloud.taxpreference.services.message.entity.dos;

import cn.huacloud.taxpreference.common.enums.MsgType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
@TableName("t_msg_record")
public class MsgRecordDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 消息类型
     */
    private MsgType msgType;
    /**
     * 消息业务类型
     */
    private String msgBizType;
    /**
     * 发信人
     */
    private String sender;
    /**
     * 收信人
     */
    private String receiver;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 消息参数
     */
    private String msgParam;
    /**
     * 扩展属性1
     */
    private String extendsField1;
    /**
     * 扩展属性2
     */
    private String extendsField2;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
