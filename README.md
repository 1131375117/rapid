# tax preference server 
> 税收优惠政策服务平台

本系统目标是为企业财务人员提供权威的、精准的税务优惠内容推送服务。

## 用户权限控制

项目采用Sa-Token框架来控制管理权限，只用权限注解来进行权限录入，同时需要添加自定义权限信息补充注解，具体使用方法如下：

1. 使用 @SaPermissionCheck 注解接口，value 对应的是权限码值；
2. 使用 @PermissionInfo 注解，添加权限的额外信息：权限名称、权限分组。

> PS：每个接口的权限码值不能重复，需要同时添加两个注解，缺一不可。

## 日期格式处理

在数据库设计过程中，为了避免引起歧义，需要严格区分日期类型（date）和日期时间类型（datetime），这两种日期类型使用JDK8中的新日期类LocalDate和LocalDateTime去做接收。

在接收前端请求参数时，LocalDate和LocalDateTime使用如下的形式进行转换：

* 日期: date(SQL) <-> LocalDate(java) <-> 2021-10-22(json)
* 日期时间: datetime(SQL) <-> LocalDateTime(java) <-> 2021-10-22T09:12:22(json)

## 数据库字段加密

本项目需要对核心价值数据字段进行加密处理，避免私有化部署后的数据泄露。

> 数据库字段加密算法：AES/CBC/PKCS5Padding 64位
> 技术实现：使用mybatis-plus的字段类型处理器，来实现数据库字段的自动加解密

```java
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class UserDO {
    private Long id;
    /**
     * 注意！！ 必须开启映射注解
     * @TableName(autoResultMap = true)
     */
    @TableField(typeHandler = AESEncryptTypeHandler.class)
    private String secretInfo;
}
```

## 服务部署与更新
[服务器部署与更新](docs/deploy.md)
