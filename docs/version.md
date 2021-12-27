# 版本与改动内容

---

## v.test.0.0.11

* 后台bug修复；
* 前台bug修复；
* 税收优惠检索增加动态条件筛选。

---

## v.test.0.0.10

### 政策法规

* 修改文号，添加字号、序号和年号；
* 修改校验，政策法规只做标题和正文校验，文号不查重；
* 修改摘要字段，政策摘要截取正文前200字；
* 政策法规增加所属专题；
* 修改所属税种，从单选变成多选；
* 去除适用企业类型和纳税人资格认定类型条件。

### 税收优惠

* 删除适用行业，有效性，企业类型变更输入框，新增政策法规具体条款字段，纳税信用等级更改为参数配置；
* 保存接口去除非必要校验；
* 新增税收优惠点击提交不许要审核自动通过；
* 点击税收优惠发布接口需要对保存的字段进行必填校验并不需要审核；
* 税收优惠删除申报条件新增自定义条件；
* 内容审核列表暂时弃用。