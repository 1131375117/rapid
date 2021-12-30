package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;

import java.util.function.Function;

/**
 * 文号处理器
 * @author wangkh
 */
public interface DocCodeProcessors {

    /**
     * 文号处理器
     */
    Function<String, DocCodeVO> docCode = codeStr -> {
        DocCodeVO docCodeVO = new DocCodeVO();
        // TODO 目前充斥着各种异常文号，待需求确认
        docCodeVO.setDocWordCode(codeStr);
        return docCodeVO;
    };
}
