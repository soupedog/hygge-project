# Links
- [中文文档](https://github.com/soupedog/hygge-project/wiki/Document_ch)

# Introduction

This project is an out-of-the-box, providing common tools and template classes for java web application development, its usage is very similar to ``spring-boot-starter-web``.

``hygge-project`` Includes the following sub-modules:

- hygge-commons: hygge core entity(includes Exception、POJO)
- hygge-commons-spring-boot: For initializing ``HyggeSpringContext`` to provide support to other modules
- hygge-logging: Simplify the logging framework configuration and make the logs have good support for exporting json format records
- hygge-utils: Tools for data transformation, validation, iteration, etc.
- hygge-web-toolkit: Provide web application development template tools and network request tools

``hygge-web-toolkit`` is the core library in ``hygge-project``, which aggregates ``hygge-commons``, ``hygge-commons-spring-boot``, ``hygge-logging``, ``hygge-utils`` modules. Once you have introduced ``hygge-web-toolkit`` to your project, you get most of the features of ``hygge-project``.

***Tips：** After introducing ``hygge-web-toolkit``, ``spring-boot-starter-web`` will be introduced automatically, no additional configuration is needed*

# Quick Start

Make sure you provide a name for the application, for example:
in ``application.properties``, you have added
```properties
spring.application.name=xxx
``` 

## Maven Dependencies

```xml
<dependency>
  <groupId>io.github.soupedog</groupId>
  <artifactId>hygge-web-toolkit</artifactId>
  <version>0.0.1</version>
</dependency>
```

``hygge-web-toolkit`` relies on ``spring-boot`` related components, but no forced dependency on the ``spring-boot`` version, you can actively specify version, as in the following

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.7.4</version>
  <relativePath/> <!-- lookup parent from repository -->
</parent>
``` 


## Features

After introducing  ``hygge-web-toolkit`` and you will obtain the following features of ``hygge-project``:

- Global unified semantic exceptions.
- Automatically configure the logging framework so that the application has good support for outputting ``json`` format logs. The following logging frameworks are supported:
  - logback
  - log4j
- Network request tool based on ``RestTemplate`` , each API call can be individually configured with parameters such as timeout time, automatic request logging, providing convenient and practical syntax sugar.
- Some data validation, fetching, iterating and other tools that are often needed in your development.
- Standardized exposed endpoint template tool.
- Automatic logging the request and response for exposed endpoints.

## Samples

The basic runnable sample project see: [web-application-example](https://github.com/soupedog/hygge-project/tree/main/web-application-example)
