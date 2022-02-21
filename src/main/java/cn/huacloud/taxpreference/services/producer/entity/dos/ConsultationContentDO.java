package cn.huacloud.taxpreference.services.producer.entity.dos;

import cn.huacloud.taxpreference.common.enums.ContentType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description 热门咨询表
 * @author zhengkai.blog.csdn.net
 * @date 2022-02-17
 */
@Data
@TableName("t_consultation_content")
public class ConsultationContentDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
    * id
    */
    private Long id;

    /**
    * consultation_id
    */
    private Long consultationId;

    /**
    * 咨询内容类型(question，answer)
    */
    private ContentType contentType;

    /**
    * 文本内容
    */
    private String content;

    /**
    * 咨询图片
    */
    private String imageUris;

    /**
    * 排序字段
    */
    private Integer sort;

    /**
    * create_time
    */
    private LocalDateTime createTime;

}