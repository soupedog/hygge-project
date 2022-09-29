# hygge-project 简介
作者在日常的工作的实践中发现，代码质量腐败变质通常是由于想偷懒引起的，为了满足短期需要，在当前项目里一切从简以 ”能跑起来就行“ 为目标，闭门造车重复制造了只能自己用又包含很多缺憾的 “轮子” 而引起的。

本项目则是基于上述痛点，基于 Spring Boot 生态二次封装，任何基于 ``spring`` 的 web 应用开箱即用，自动装配了常见业务开发所需依赖与工具的库。

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

- 自动进行日志框架配置，使应用对输出 ``json`` 格式日志有良好的支持，目前已支持的日志框架
  - logback
  - log4j
- 全局统一的，语义化明确的异常
- 基于 ``RestTemplate`` 二次封装的网络请求工具，可精确到特定 API 的请求参数配置、请求自动日志记录和更便捷实用的语法糖
- 自动记录应用对外暴露端点的出入参日志
- 标准化的对外暴露端点的模板工具
- 业务开发中经常会需要的一些数据校验、取值、遍历等工具

## 示例项目

最基础可执行项目样例工程可参见 ``web-application-example``
