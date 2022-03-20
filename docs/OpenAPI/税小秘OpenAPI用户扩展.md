# 税小秘OpenAPI用户扩展

## OpenUserId请求头参数

### 说明
<p>像用户收藏、浏览记录等功能接口，需要OpenAPI调用方提供正在调用此接口的用户ID（OpenUserId可以加密，类似微信OpenId，保证唯一性即可）。

###传参方式
在接口调用时统一添加名为`OpenUserId`的请求请求头。

### 示例（最后一行为添加示例）
```text
POST /open-api/v1/queryOperationRecord HTTP/1.1
Host: tax-open.hua-cloud.net.cn
Connection: keep-alive
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36
X-Requested-With: XMLHttpRequest
Accept: */*
Referer: https://tax-open.hua-cloud.net.cn/home
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9
OpenUserId: 13
```

###必须传OpenUserId的接口
* 添加到收藏列表
* 查询收藏列表
* 添加操作记录
* 查询操作记录

> Tips: 其他接口均建议传递参
