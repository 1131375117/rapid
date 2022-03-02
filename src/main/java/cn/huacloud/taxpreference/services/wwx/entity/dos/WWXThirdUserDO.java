package cn.huacloud.taxpreference.services.wwx.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
@TableName("t_wwx_third_user")
public class WWXThirdUserDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String corpId;
    private String userId;
    private String name;
    private Integer parentId;
    private String position;
    private String gender;
    private String email;
    private String isLeaderInDept;
    private String avatar;
    private String thumbAvatar;
    private String telephone;
    private String alias;
    private String address;
    private String openUserId;
    private Integer mainDepartment;
    private String qrCode;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
