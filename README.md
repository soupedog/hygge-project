<p align="center">
  Born To Simplify Java Web Application Development
</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:io.github.soupedog%20AND%20a:hygge-web-toolkit">
    <img alt="maven" src="https://img.shields.io/maven-central/v/io.github.soupedog/hygge-web-toolkit.svg?style=flat-square">
  </a>

<a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

# Links

- [中文文档](https://github.com/soupedog/hygge-project/wiki/Document_ch)

# Introduction

- Do you struggle with the design of a unified exception system with high scalability?
- Do you struggle with the lack of Rest API request/response logs?
- Do you struggle with the lack of a powerful Http request tool?
- Do you struggle with the configuration of a trivial and cumbersome logging system?
- Do you struggle with the interconversion between basic data types and the assignment of default values to parameters?

If you answered yes, then I would strongly recommend you to use the ``hygge-project``.

| Building Block            | Description                                                                                                        |
|---------------------------|--------------------------------------------------------------------------------------------------------------------|
| hygge-commons             | hygge core entity(includes Exception、POJO)                                                                         |
| hygge-commons-spring-boot | For initializing ``HyggeSpringContext`` to provide support to other modules                                        |
| hygge-logging             | Simplify the logging framework configuration and make the logs have good support for exporting json format records |
| hygge-utils               | Tools for data transformation, validation, iteration, etc.                                                         |
| hygge-web-toolkit         | Provide web application development template tools and network request tools                                       |

## JDK Requirement

Java SDK v1.8 or higher

***Warning:** this project relies on spring-boot, and if spring-boot drops low version java in the future, this project
will also drop support for low version java.*

## Features

``hygge-web-toolkit`` is designed as a ``starter`` for ``spring-boot``. It also depends on other libraries, and the
following dependencies, which you no longer need to add manually, will be added to the project automatically:

- Hygge-Project
    - hygge-commons
    - hygge-commons-spring-boot
    - hygge-logging
    - hygge-utils
- Third Party Dependencies
    - spring-boot-starter-web
    - spring-boot-starter-aop
    - httpclient

After you import the ``hygge-web-toolkit`` into your project, you will obtain the following features
of ``hygge-project``:

- Global unified semantic exceptions.
- Automatically configure the logging framework so that the application has good support for outputting ``json`` format
  logs. The following logging frameworks are supported:
    - logback
    - log4j
- Network request tool based on ``RestTemplate`` , each API call can be individually configured with parameters such as
  timeout time, automatic request logging, providing convenient and practical syntax sugar.
- Some data validation, fetching, iterating and other tools that are often needed in your development.
- Standardized exposed endpoint template tool.
- Automatic logging the request and response for exposed endpoints.

# Quick Start

Make sure you provide the name for the application, otherwise it will not be allowed to start.

For example: You have to add ``spring.application.name`` to your ``application.properties``

```properties
spring.application.name=xxx
```

## Maven Dependencies

<p align="center">
  <a href="https://search.maven.org/search?q=g:io.github.soupedog%20AND%20a:hygge-web-toolkit">
    <img alt="maven" src="https://img.shields.io/maven-central/v/io.github.soupedog/hygge-web-toolkit.svg?style=flat-square">
  </a>
</p>

If you are using Maven just add the following dependency to your pom.xml:

```xml

<dependency>
    <groupId>io.github.soupedog</groupId>
    <artifactId>hygge-web-toolkit</artifactId>
    <version>${latest.version}</version>
</dependency>
```

``hygge-web-toolkit`` relies on ``spring-boot`` related components, but no forced dependency about the ``spring-boot``
version, you can specify special versions in any of the following ways

- ``<parent/>``

  ```xml
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>${Your specific spring-boot version}</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  ```
- ``<dependencyManagement/>``

  ```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>${Your specific spring-boot version}</version>
      </dependency>
      <!-- Omit the other spring-boot components you need-->
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <version>${Your specific spring-boot version}</version>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

## Samples

The basic runnable sample
project: [web-application-example](https://github.com/soupedog/hygge-project/tree/main/web-application-example)
