package cn.huacloud.taxpreference.services.producer.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;


/**
 * @author wuxin
 */

public enum CheckStatus implements IEnum<String> {

    /**
     *
     */
  OK("删除政策政策法规会自动将已关联录入的政策解读、热点问答全部删除，请确认是否继续操作！"),
  INFO("删除政策政策法规会自动将已关联录入的政策解读、热点问答和税收优惠全部删除，请确认是否继续操作！"),
  CAN_NOT("当前政策已关联税收优惠事项，请先删除优惠事项,在进行政策删除操作！");

  private final String name;

  CheckStatus(String name) {
    this.name = name();
  }

  @Override
  public String getValue() {
    return this.name();
  }
}
