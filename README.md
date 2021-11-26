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

为了节省服务器资源，税收优惠系统采用以dev、test后缀区分数据库、索引名称，把测试环境和开发环境部署到了同一台服务器。一下是开发、测试环境服务更新方法：

### 后端服务更新方法

1. 使用IDE maven工具 clean -> package -> plugins.dockerfile.build（在执行build前，需要添加docker环境变量）
2. 使用shell工具登录服务器，服务端查看镜像是否更新 docker images | grep tax
3. cd到compose脚本目录：cd /opt/docker/tax-preference/dev-server/tax-preference（发版测试环境路径为：/opt/docker/tax-preference/test-server/tax-preference）
4. 启动、更新服务：docker-compose up -d

> PS: docker环境变量添加方法 
> 1. 本地windows添加环境变量：DOCKER_HOST tcp://docker-host:2375
> 2. 本地windows添加host配置：172.16.122.17 docker-host
> 3. 配置完成需要重新启动IDEA，否则环境变量不会生效

### 前端静态文件更新方法

1. 上传文件至 /opt/docker/tax-preference/nginx/web/{发布环境dev或者test}/{所属前端项目web或者manage} 下，web是前台页面，manage是后台管理
2. 解压zip包覆盖旧文件 unzip -o dist.zip

> PS: 前端静态文件发布后想要查看效果，需要刷浏览器页面，若前端发布仍有问题，可以尝试彻底删除旧文件再进行解压。

## 应用程序发版待办列表

* 打包构建应用镜像，包含前端前台nuxt镜像和后端服务镜像
* 使用base-server.yml启动基础服务
* 修改nginx配置信息，添加前端静态文件
* 数据库添加数据库和用户，执行初始化SQL建表和添加管理员用户
* 执行系统码值数据SQL添加系统码值
* RabbitMQ添加对应virtual host和用户
* Minio添加对应用户
* Elasticsearch添加对应索引模板
* 启动应用服务，调用es数据同步接口，同步数据库中的数据
* 启动前台nuxt服务镜像
* 打开前台页面，登录后台管理系统验证功能