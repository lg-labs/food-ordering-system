zookeeper-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml down --volumes
kafka-cluster-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml down --volumes
kafka-init-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml down --volumes
kafka-mngr-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_mngr.yml down --volumes
ddbb-down:
	docker-compose -f ${INFRA}/docker-compose-ddbb.yml down --volumes --volumes


zookeeper-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml up -d
kafka-cluster-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml up -d
kafka-init-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml up -d
kafka-mngr-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_mngr.yml up -d
ddbb-up:
	docker-compose -f ${INFRA}/docker-compose-ddbb.yml up -d

docker-down: zookeeper-down kafka-cluster-down kafka-init-down kafka-mngr-down ddbb-down

docker-up: zookeeper-up kafka-cluster-up kafka-init-up kafka-mngr-up ddbb-up


## APPs
run-app:
	mvn -pl ${APP} -am spring-boot:run


run-happy-path: docker-down docker-up run-app


# KAFKA MODELS from Avro Model definition
# If add a new Avro model, REMEMBER execute kafka model again.
run-avro-model:
	mvn -pl ${AVRO_MODEL} clean install

APP = order-container
INFRA = order-container/src/test/resources/docker
AVRO_MODEL = order-message/order-message-model

CUSTOMER_APP = customer-service/customer-container
RESTAURANT_APP = restaurant-service/restaurant-container
PAYMENT_APP = payment-service/payment-container

