package com.labs.lg.food.ordering.system.order.service.domain.outbox;

import com.labs.lg.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.labs.lg.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.labs.lg.food.ordering.system.order.service.domain.boot.Bootstrap;
import com.labs.lg.food.ordering.system.order.service.domain.OrderPaymentSaga;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.saga.SagaStatus;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.labs.lg.food.ordering.system.order.service.domain.saga.SagaConstants.ORDER_SAGA_NAME;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Slf4j
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetUp.sql"})
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
class OrderPaymentSagaIT extends Bootstrap {

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");
    private final UUID ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17");
    private final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");
    private final UUID PAYMENT_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment1() {


        Response response = given(requestSpecification)
                .when()
                .get("/actuator/health");
        response.then().statusCode(HttpStatus.OK.value());
        response.then().log().ifValidationFails(LogDetail.ALL);

        await().timeout(Duration.ofSeconds(30)).untilAsserted(() -> {
            var healthService = response.then()
                    .extract()
                    .body()
                    .jsonPath().getString("status");
            assertEquals("UP", healthService);
        });
        assertTrue(Boolean.TRUE);
    }

    @Test
    void it_should_completed_payment_but_try_again_the_payment_should_is_already_processed() {
        orderPaymentSaga.process(getPaymentResponse());
        orderPaymentSaga.process(getPaymentResponse());
    }

    @Test
    void it_should_completed_payment_but_try_again_the_payment_should_is_already_processed_With_Threads() throws InterruptedException {
        Thread thread1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        Thread thread2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertPaymentOutbox();
    }

    @Test
    void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread1");
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread2");
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch.await();

        assertPaymentOutbox();

    }

    private void assertPaymentOutbox() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID,
                        List.of(SagaStatus.PROCESSING));
        assertTrue(paymentOutboxEntity.isPresent());
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(com.labs.lg.food.ordering.system.order.service.domain.valueobject.PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }

}
