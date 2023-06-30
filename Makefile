docker-zookeeper-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml down --volumes
docker-kafka-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml down --volumes
docker-kafka-init-down:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml down --volumes

docker-ddb-down:
	docker-compose -f ${INFRA}/docker-compose-ddbb.yml down --volumes


docker-zookeeper-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/zookeeper.yml up -d
docker-kafka-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_cluster.yml up -d
docker-kafka-init-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/init_kafka.yml up -d
docker-kafka-mngr-up:
	docker-compose -f ${INFRA}/common.yml -f ${INFRA}/kafka_mngr.yml up -d
docker-ddb-up:
	docker-compose -f ${INFRA}/docker-compose-ddbb.yml up -d

docker-down: docker-zookeeper-down docker-kafka-down docker-kafka-init-down docker-ddb-down

docker-up: docker-zookeeper-up docker-kafka-up docker-kafka-init-up docker-ddb-up

# Filebeat to ELK
docker-filebeat-down:
	docker-compose  -f ${INFRA}/filebeats/docker-compose.yml down
docker-filebeat-up:
	docker-compose  -f ${INFRA}/filebeats/docker-compose.yml up -d


## APPs

run-order:
	mvn -pl order-service/order-container -am spring-boot:run

run: run-order

INFRA = infrastructure/docker-compose