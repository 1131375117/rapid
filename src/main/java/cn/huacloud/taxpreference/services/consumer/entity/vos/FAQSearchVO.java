package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author wangkh
 */
@Data
public class FAQSearchVO extends UserCollectionInfo {

    @ApiModelProperty(value = "主键ID", example = "7")
    private Long id;

    @ApiModelProperty(value = "标题", example = "机关、企事业单位统一组织员工开展公益捐赠的，纳税人可以依据什么在个人所得税税前扣除")
    private String title;

    @ApiModelProperty(value = "内容", example = "据《财政部 税务总局关于公益慈善事业捐赠个人所得税政策的公告》（财政部 税务总局公告2019年第99号）第九条的规定： 公益性社会组织、国家机关在接受个人捐")
    private String content;

    @ApiModelProperty(value = "来源", example = "国家税务总局辽宁省税务局")
    private String docSource;

    @ApiModelProperty(value = "发布日期", example = "2020-03-20")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "无装饰的组合文本", example = "号）第九条的规定： 公益性社会组织、国家机关在接受个人捐赠时，应当按照规定开具捐赠票据；个人索取捐赠票据的，应予以开具。 个人发生公益捐赠时不能及时取")
    private String combinePlainContent;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty(value = "浏览量", example = "0")
    private Long views;

    @ApiModelProperty(value = "收藏量", example = "0")
    private Long collections;

    @ApiModelProperty(value = "解答机构", example = "国家税务总局辽宁省税务局")
    private String answerOrganization;
}
