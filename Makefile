zookeeper-down:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml down --volumes
kafka-cluster-down:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml down --volumes
kafka-init-down:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml down --volumes
kafka-mngr-down:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_mngr.yml down --volumes
ddbb-down:
	docker compose -f ${INFRA}/docker-compose-ddbb.yml down --volumes --volumes


zookeeper-up:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml up -d
kafka-cluster-up:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml up -d
kafka-init-up:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml up -d
kafka-mngr-up:
	docker compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_mngr.yml up -d
ddbb-up:
	docker compose -f ${INFRA}/docker-compose-ddbb.yml up -d

docker-down: zookeeper-down kafka-cluster-down kafka-init-down kafka-mngr-down ddbb-down

docker-up: zookeeper-up kafka-cluster-up kafka-init-up kafka-mngr-up ddbb-up

build_to_arm:
	 mvn clean install -Parch-aarch64
build_to_amd:
	mvn clean install -Pamd

clean:
	mvn clean
# INSTALL ARTIFACT
install: clean
	mvn install

install-skip-test: clean
	mvn install -DskipTests

# TESTING
run-checkstyle:
	mvn validate
run-verify: clean
	mvn verify

run-atdd-module:
	mvn -pl ${ATDD} clean install -Dapplication.traces.file.enabled=${FILE_LOG}

## APPs
run-customer:
	mvn -pl ${CUSTOMER_APP} -am spring-boot:run

run-order:
	mvn -pl ${ORDER_APP} -am spring-boot:run
run-restaurant:
	mvn -pl ${RESTAURANT_APP} -am spring-boot:run

run-payment:
	mvn -pl ${PAYMENT_APP} -am spring-boot:run



run-apps: run-customer run-order run-restaurant run-payment

run-happy-path: docker-down docker-up run-apps


# KAFKA MODELS from Avro Model definition
# If add a new Avro model, REMEMBER execute kafka model again.
run-kafka-model:
	mvn -pl ${KAFKA_MODEL} clean install

INFRA = infrastructure/docker-compose
CUSTOMER_APP = customer-service/customer-container
ORDER_APP = order-service/order-container
RESTAURANT_APP = restaurant-service/restaurant-container
PAYMENT_APP = payment-service/payment-container

KAFKA_MODEL = infrastructure/kafka/kafka-model

ATDD ?= blank-acceptance-test
FILE_LOG ?=false