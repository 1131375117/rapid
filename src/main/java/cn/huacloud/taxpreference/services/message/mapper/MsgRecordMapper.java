package cn.huacloud.taxpreference.services.message.mapper;

import cn.huacloud.taxpreference.common.enums.MsgType;
import cn.huacloud.taxpreference.services.message.entity.dos.MsgRecordDO;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 消息记录操作实体对象
 * @author wangkh
 */
@Repository
public interface MsgRecordMapper extends BaseMapper<MsgRecordDO> {


    /**
     * 获取规定时限内最近一条记录的创建时间， 没有记录返回null
     * @param msgType 消息类型
     * @param msgBizType 消息业务类型
     * @param singleReceiver 单个收信人
     * @param limitSeconds 限制秒数
     * @return 创建时间，没有返回null
     */
    default LocalDateTime getLimitLastCreateTime(MsgType msgType, IEnum<String> msgBizType, String singleReceiver, long limitSeconds) {
        LocalDateTime fromTime = LocalDateTime.now().minus(limitSeconds, ChronoUnit.SECONDS);
        return selectLimitLastCreateTime(msgType.getValue(), msgBizType.getValue(), singleReceiver, fromTime);
    }

    /**
     * 获取规定时限内最近一条记录的创建时间， 没有记录返回null
     * @param msgType 消息类型
     * @param msgBizType 消息业务类型
     * @param singleReceiver 单个收信人
     * @param fromTime 开始时间
     * @return 创建时间，没有返回null
     */
    LocalDateTime selectLimitLastCreateTime(@Param("msgType") String msgType,
                                            @Param("msgBizType") String msgBizType,
                                            @Param("singleReceiver") String singleReceiver,
                                            @Param("fromTime") LocalDateTime fromTime);
}
