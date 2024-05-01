# 🛠️ Order μ-service
[![lg-labs][0]][1]
[![License][2]][LIC]

<img src="https://avatars.githubusercontent.com/u/105936384?s=400&u=290ae673580a956864a07d4aef8e4448372a836b&v=4" align="left" width="172px" height="172px"/>
<img align="left" width="0" height="172px" hspace="10"/>

> 👋 Management the order service for the food ordering system.
>

From **Lg Pentagon** or **lg5**! Get [Lg5-Spring][4] to develop μ-services faster.

For more information, check this pages https://lufgarciaqu.medium.com.
<h1></h1>

# Using Lg5 Spring `1.0.0-alpha`, JDK 21

[More details][4]


## 🚀 Build project

Install 1/1: Setup JDK 21.

```bash
sdk use java 21.0.2-amzn 
```

Install 1/2: Install the dependencies in your project.

```bash
mvn clean install 
```
## 🚀 Deploy with K8s

Use the infra repository [food-ordering-system-infra][8] to deploy with **K8s**

## 📚Contents

* [order-acceptance-test](order-acceptance-test)
* [order-api](order-api)
* [order-container](order-container)
* [order-data-access](order-data-access)
* [order-domain](order-domain)
* [order-message](order-message)


## 🚀 Run locally

### You can ...
Using `makefile`

### Start with infrastructure
😀 To **start** the Kafka Cluster and Postgres.

```shell
make docker-up
```

⛔️ To the Kafka Cluster and Postgres **stop** or **destroy**:
```shell
make docker-down
```

### Run APP
😀 To **start** the Order Service.

```shell
make run-app
```

#### Order API `1.0.0-alpha`
> 👋  **[Order API, Port:8181][5]**
>
> Username: `None`  
> Password: `None`

# Contracts

1. [Open API][6]
2. [Async API][7]

## AVRO MODELS from Avro Model definition
> If you add a new Avro model, REMEMBER execute avro model again.
```shell
make run-avro-model
```
## Logger & ELK
This project is prepared to send log files and process visualization with filebeat.
You can specify the directory for stored the *.log files. Now, genera two file logs.

> Simple log
>* [log.path]/[application_name]-simple.log
> 
> Complex log
>* [log.path]/[application_name]-complex.log
>
- Specify the directory with `log.path` property.

**_Simple_**: `Simple details about application logs.`
**_Complex_**:  `More details about application logs.`



## ⚖️ License

The MIT License (MIT). Please see [License][LIC] for more information.


[0]: https://img.shields.io/badge/LgLabs-community-blue?style=flat-square
[1]: https://lufgarciaqu.medium.com
[2]: https://img.shields.io/badge/license-MIT-green?style=flat-square
[4]: https://github.com/lg-labs-pentagon/lg5-spring/README.md

[5]: http://localhost:8181
[6]: order-api/src/main/resources/spec/openapi.yaml
[7]: order-message/order-message-model/src/main/resources/spec/asyncapi.yaml
[8]: https://github.com/lg-labs/food-ordering-system-infra

[LIC]: LICENSE

[img1]: https://github.com/lg-labs-pentagon/lg-labs-boot-parent/assets/105936384/31c27db8-1e77-478d-a38e-7acf6ba2571c
