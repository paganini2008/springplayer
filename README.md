# SpringPlayer Framework

<code>SpringPlayer</code> is a quick-start template framework for developing project based on <code>SpringCloud Framework</code>. It aim to  help developer rapidly build project development environment and  focus on  implementing on business logical code rather than maintaining repeated basic code of framework self.



## Compatibility

1. Jdk8 (or later)
2. `SpringBoot` Framework 2.3.12.RELEASE
3. <code>SpringCloud</code> Framework Hoxton.SR12
4. `Redis` 4.x (or later)
5. MySQL 8.x



## Hierachy

``` shell
springplayer
├── README.md
├── spring-player-dependencies
├── spring-player-codegen
├── spring-player-common
│   ├── spring-player-common-dependencies
│   │   ├── spring-player-common-amqp
│   │   ├── spring-player-common-apollo
│   │   ├── spring-player-common-cache
│   │   ├── spring-player-common-crumb
│   │   ├── spring-player-common-data
│   │   ├── spring-player-common-dingtalk
│   │   ├── spring-player-common-elasticsearch
│   │   ├── spring-player-common-email
│   │   ├── spring-player-common-feign
│   │   ├── spring-player-common-file
│   │   ├── spring-player-common-hystrix
│   │   ├── spring-player-common-i18n
│   │   ├── spring-player-common-id
│   │   ├── spring-player-common-jdbc
│   │   ├── spring-player-common-jpa
│   │   ├── spring-player-common-kafka
│   │   ├── spring-player-common-log
│   │   ├── spring-player-common-messenger
│   │   ├── spring-player-common-monitor
│   │   ├── spring-player-common-mybatis
│   │   ├── spring-player-common-oauth
│   │   ├── spring-player-common-quartz
│   │   ├── spring-player-common-redis
│   │   ├── spring-player-common-sentinel
│   │   ├── spring-player-common-swagger
│   │   ├── spring-player-common-sysinfo
│   │   ├── spring-player-common-transaction
│   │   ├── spring-player-common-upms
│   │   ├── spring-player-common-validation
│   │   ├── spring-player-common-vm
│   │   ├── spring-player-common-web
│   │   ├── spring-player-common-xxljob
│   │   └── spring-player-common-zookeeper
│   └── spring-player-midware-dependencies
│       ├── spring-player-common-gateway
│       ├── spring-player-common-webflux
│       ├── spring-player-common-webmvc
│       ├── spring-player-common-websocket
│       ├── spring-player-webflux-dependencies
│       └── spring-player-webmvc-dependencies
└── spring-player-midware
     ├── spring-player-application-template
     ├── spring-player-application-webflux-template
     ├── spring-player-channel-service
     ├── spring-player-crumb-service
     ├── spring-player-file-service
     ├── spring-player-gateway-service
     ├── spring-player-i18n-service
     ├── spring-player-id-service
     ├── spring-player-log-service
     ├── spring-player-messenger-service
     ├── spring-player-oauth-service
     ├── spring-player-popup-service
     ├── spring-player-registry-service
     ├── spring-player-sentinel-service
     └── spring-player-upms-service

```



## Quick Start

#### Install Spring Mvc Application

``` shell
cd springplayer/spring-player-midware/spring-player-application-template
mvn clean
mvn archetype:create-from-project
cd target/generated-sources/archetype/
mvn install
mvn archetype:crawl
mvn archetype:update-local-catalog
mvn archetype:generate

```


#### Install Spring Webflux Application

``` shell
cd springplayer/spring-player-midware/spring-player-application-webflux-template
mvn clean
mvn archetype:create-from-project
cd target/generated-sources/archetype/
mvn install
mvn archetype:crawl
mvn archetype:update-local-catalog
mvn archetype:generate

```
