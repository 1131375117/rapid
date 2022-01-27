package cn.huacloud.taxpreference.services.common.mapper;

import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件数据操作
 * @author wangkh
 */
@Repository
public interface AttachmentMapper extends BaseMapper<AttachmentDO> {

    /**
     * 获取最新更新的附件名称
     * @param path 附件路径
     * @return 附件名称
     */
    default String getLastAttachmentName(String path) {
        LambdaQueryWrapper<AttachmentDO> queryWrapper = Wrappers.lambdaQuery(AttachmentDO.class)
                .eq(AttachmentDO::getPath, path)
                .orderByDesc(AttachmentDO::getCreateTime);

        List<AttachmentDO> attachmentDOList = selectList(queryWrapper);
        if (attachmentDOList.isEmpty()) {
            return null;
        }
        return attachmentDOList.get(0).getAttachmentName()+"."+ StringUtils.substringAfterLast(path,".");
    }
}
