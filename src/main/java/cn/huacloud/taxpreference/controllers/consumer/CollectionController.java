package cn.huacloud.taxpreference.controllers.consumer;

import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏功能
 *
 * @author fuhua
 **/
@Api(tags = "收藏功能")
@RequiredArgsConstructor
@RequestMapping("/api/v1/collection")
@RestController
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * 收藏功能:点击收藏添加到我的收藏,再次点击取消收藏,未登录用户需要先登录
     * 参数列表：docId,收藏类型
     */
    @ApiOperation("点击收藏功能")
    @ConsumerUserCheckLogin
    public ResultVO<Void> saveOrCancelCollection(@Validated CollectionDTO collectionDTO) {
    @PutMapping("/collection")
    public ResultVO<Boolean> saveOrCancelCollection(@Validated CollectionDTO collectionDTO) {
        collectionDTO.setConsumerUserId(ConsumerUserUtil.getCurrentUser().getId());
        Boolean isCollection = collectionService.saveOrCancelCollection(collectionDTO);
        return ResultVO.ok(isCollection).setMsg("收藏状态是否收藏");
    }

    /**
     * 收藏展示功能:
     * 参数列表：无
     */
    /*@ApiOperation("我的收藏展示")
    @PostMapping("/queryCollection")
    public ResultVO<PageVO<CollectionVO>> queryCollection(PageQueryDTO pageQueryDTO) {
        PageVO<CollectionVO> pageVO = collectionService.queryCollection(pageQueryDTO,ConsumerUserUtil.getCurrentUser().getId());
        return ResultVO.ok(pageVO);
    }
}
