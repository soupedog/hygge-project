# hygge-project 简介
作者在日常的工作的实践中发现，代码质量腐败变质通常是由于想偷懒引起的，为了满足短期需要，在当前项目里一切从简以 ”能跑起来就行“ 为目标，闭门造车重复制造了只能自己用又包含很多缺憾的 “轮子” 而引起的。

本项目则是基于上述痛点，基于 Spring Boot 生态二次封装，基于 Spring Boot 生态二次封装，任何基于 spring 的 web 应用可开箱即用，自动装配了常见业务开发所需依赖与工具的库。
``hygge-web-toolkit`` 是 ``hygge-project`` 中最核心的库，它整合了多个 ``hygge-project`` 下的多个子库。一旦你为自己的项目引入了 ``hygge-web-toolkit``，你就获取到了 ``hygge-project`` 的绝大多数特性。

***Tips：** ``hygge-web-toolkit`` 是在 ``spring-boot-starter-web`` 的基础上进行拓展，对业务代码的入侵性低，不会过多干预代码开发的自由度，旨在为规范化 web 应用开发提供一个基石。如果你熟悉为自己的应用引入 ``spring-boot-starter-web`` ，那么你同样会对引入 ``hygge-web-toolkit`` 感到亲切，他们的使用方法极其相似。*

# 快速开始

## maven 依赖

```xml
<dependency>
    <groupId>io.github.soupedog</groupId>
    <artifactId>hygge-web-toolkit</artifactId>
    <version>0.0.1</version>
</dependency>
```

## ``hygge-web-toolkit`` 的主要功能
现阶段，只需引入 ``hygge-web-toolkit`` 即可享用到 ``hygge-project`` 的下列功能：

- 全局统一的，语义化明确的异常
- 自动进行日志框架配置，使应用对输出 ``json`` 格式日志有良好的支持，目前已支持的日志框架
  - logback
  - log4j
- 基于 ``RestTemplate`` 二次封装的网络请求工具，可精确到特定 API 的请求参数配置、请求自动日志记录和更便捷实用的语法糖
- 自动记录应用对外暴露端点的出入参日志
- 标准化的对外暴露端点的模板工具
- 业务开发中经常会需要的一些数据校验、取值、遍历等工具

## 示例项目

最基础可执行项目样例工程可参见 ``web-application-example``


# 开发者参考书

## hygge 异常
``hygge-project`` 有全局的统一异常定义，相关代码可参见 ``hygge-commons`` 模块的 ``hygge.commons.exceptions`` 包。

这些异常分为两大类，运行时异常和检查型异常。有相同名称前缀的异常都是类似的，仅仅是明确告知使用者哪些必须主动捕获并处理，而哪些不需要。

举例：
``InternalException``、``InternalRuntimeException`` 

### HyggeCode
每一个 ``hygge-project`` 定义的异常都具备返回 ``HyggeCode`` 的能力，下列

```java
public interface HyggeCode<C, E> {
    boolean serious();
    String getPublicMessage();
    <Co> Co getCode();
    <Ex>Ex getExtraInfo();
}
```

- ``serious()`` 用于告知异常的捕获者，这个异常是可以自愈还是需要开发人员人工介入检查的，如果返回 ``false`` ，那么则认为这个异常应用正常运行时符合预期的异常，不代表应用功能不可用。
- ``getPublicMessage()`` 用于隐藏异常原始信息，并指定已脱敏可对外暴露的公开信息。应用场景例子：如果数据库操作语句执行失败，有可能会返回具体执行的 ``sql`` 信息，我们可以通过这个强制用已脱敏的 "服务器正忙，请稍后再试。" 返回给客户端。
  - 如果该方法返回 ``null``：意味着该异常携带的信息是无需对外保密的，``Throwable.getMessage()`` 可能会直接暴露给调用方。
  - 如果该方法返回字符串：意味着该异常携带的信息是需对外保密的，应用日志里仍会忠实记录 ``Throwable.getMessage()`` 内容，但对客户端暴露的信息默认为 ``getPublicMessage()`` 返回的信息。
- ``getCode()`` 用于指定当前异常异常对应的业务异常。这是体现异常语义，我们推荐任何业务异常都应该具有一个独立的 ``业务异常码``。我们可以发现，``业务异常码`` 类型是不固定的，开发者可以根据自行需要指定为数字、字符串或是别的什么。例如 100 代表 token 无效——已过期、101 代表 token 无效——不存在……
- ``getExtraInfo()`` 这个方法不常用，用于令异常可携带除字符串以外的额外信息，默认均为 ``null``。你可以把它当做 ``Context`` 容器使用，便于为异常捕获者提供一些额外信息，如果需要的话。

### 异常清单
基于 web 应用开发场景，默认提供了下列异常

| 名称    | ``serious()`` 默认值 | ``getPublicMessage()`` 默认值 | ``getCode()`` 默认值 | 备注                                        |
| --- | --- | ------ | ------ | ------------ |
| External* | true              | "External Server Error"    |          GlobalHyggeCode.EXTERNAL_SYSTEM_EXCEPTION                  | 用于表示当前服务调用外部依赖发生异常                        |
| Internal* | true              | "Internal Server Error"    |          GlobalHyggeCode.SERVER_END_EXCEPTION                  | 用于表示当前服务内部发生未知异常或影响服务正常运行的异常              |
| Util* | true              | "Internal Server Error"                       |          GlobalHyggeCode.UTIL_EXCEPTION                  | 服务端内部一些工具引发的未知异常                          |
| Light* | false             | null                       |          GlobalHyggeCode.CLIENT_END_EXCEPTION                  | 用于表示当前服务因客户端而引发的错误，是当前服务正常运行时意料之内的异常      |
| Parameter* | false             | null                       |          GlobalHyggeCode.UNEXPECTED_PARAMETER                  | 用于表示参数校验不符合预期的某种错误，通常这种错误也是服务正常运行时意料之内的异常 |


当然你可以指定默认值之外的参数来创建异常，详情请自行查看异常的若干个构造函数。
