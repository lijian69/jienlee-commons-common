# teach-commons

#### 介绍
TeachApp公共 jar 包，包括 Redis、MQ，Bean等

## 3、jienlee-common-redis-autoconfigure 工具包

### 3.1 引入 maven 依赖
> 该包已经提供自动装载 spring-boot 项目中，直接引入依赖，yml 进行配置即可。
```
<dependency>
    <groupId>com.github.lijian69</groupId>
    <artifactId>jienlee-common-redis-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>
```
### 3.2 yml 配置相关的参数
```yaml
jienlee:
  redis:
    host: 49.232.241.216
    port: 6379
    database: 0
    min-idle: 0
    max-active: 8
    max-idle: 8
    max-wait: -1
    password: ""
```
### 3.3 RedisUtil 的引入
```
    RedisUtil.get(key);
    ......
```

## 3、jienlee-common-swagger-autoconfigure 工具包

### 3.1 引入 maven 依赖
> 该包已经提供自动装载 spring-boot 项目中，直接引入依赖，yml 进行配置即可。
```
<dependency>
    <groupId>com.github.lijian69</groupId>
    <artifactId>jienlee-common-swagger-autoconfigure</artifactId>
    <version>1.0.0</version>
</dependency>
```
### 3.2 yml 配置相关的参数
```yaml
jienlee:
  swagger:
    # swagger 扫描的 controller 包    
    base-package: com.start.manager.controller
    # swagger 的 title
    title: jienlee-swagger
    # swagger 的 描述
    description: swagger 接口文档
    # swagger 的 版本
    version: 1.0.0
```
### 3.3 启动项目
> 成功启动后，访问 http://ip:port/doc.html, 如下所示：

![image](https://doc.xiaominfo.com/knife4j/images/knife4j/1.png)

#### 项目时间线

1.  2020-07-07 初始化项目，新增 Redis 和 core 的工具包。




