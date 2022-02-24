package cn.huacloud.taxpreference.openapi.apis.consumer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiCheckToken;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.common.entity.dtos.CollectionQueryDTO;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByCollectionVO;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏功能
 *
 * @author fuhua
 **/
@ApiSupport(order = 800)
@Api(tags = "收藏功能")
@RequiredArgsConstructor
@RequestMapping("/open-api/v1")
@RestController
public class CollectionApi {

    private final CollectionService collectionService;

    /**
     * 收藏功能:点击收藏添加到我的收藏,再次点击取消收藏,未登录用户需要先登录
     * 参数列表：docId,收藏类型
     */
    @ApiOperation(value = "点击收藏功能", notes = "返回值boolean表示收藏的当前状态，true：已收藏；false：未收藏")
    @OpenApiCheckToken
    @PutMapping("/collection")
    public ResultVO<Boolean> saveOrCancelCollection(@RequestBody @Validated CollectionDTO collectionDTO) {
        collectionDTO.setConsumerUserId(OpenApiStpUtil.getLoginIdAsLong());
        Boolean isCollection = collectionService.saveOrCancelCollection(collectionDTO);
        return ResultVO.ok(isCollection);
    }

    /**
     * 收藏展示功能:
     * 参数列表：无
     */
    @ApiOperation("我的收藏展示")
    @OpenApiCheckToken
    @PostMapping("/queryCollection")
    public ResultVO<PageByCollectionVO> queryCollection(@RequestBody CollectionQueryDTO pageQueryDTO) {
        pageQueryDTO.paramReasonable();
        PageByCollectionVO pageVO = collectionService.queryCollection(pageQueryDTO, OpenApiStpUtil.getLoginIdAsLong());
        return ResultVO.ok(pageVO);
    }
}
