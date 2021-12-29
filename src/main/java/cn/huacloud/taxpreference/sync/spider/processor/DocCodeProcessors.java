package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;

import java.util.function.Function;

/**
 * 文号处理器
 * @author wangkh
 */
public class DocCodeProcessors {

    public static Function<String, DocCodeVO> docCode = codeStr -> {

        DocCodeVO docCodeVO = new DocCodeVO();
        return new DocCodeVO();
    };
}
