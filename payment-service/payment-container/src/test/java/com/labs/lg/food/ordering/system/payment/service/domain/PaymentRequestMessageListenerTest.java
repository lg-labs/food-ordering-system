package com.labs.lg.food.ordering.system.payment.service.domain;


import com.labs.lg.food.ordering.system.payment.service.domain.valueobject.PaymentOrderStatus;
import com.labs.lg.food.ordering.system.payment.service.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.labs.lg.food.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.labs.lg.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.lg5.spring.outbox.OutboxStatus;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.labs.lg.food.ordering.system.payment.service.domain.saga.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PaymentServiceApplication.class)
class PaymentRequestMessageListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestMessageListenerTest.class);


    private final static String CUSTOMER_ID = "d215b5f8-0249-4dc5-89a3-51fd148cfb41";
    private final static BigDecimal PRICE = new BigDecimal("100");

    @Autowired
    private PaymentRequestMessageListener paymentRequestMessageListener;

    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    @Test
    void testDoublePayment() {
        String sagaId = UUID.randomUUID().toString();
        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        } catch (DataAccessException e) {
            LOG.error("DataAccessException occurred with sql state: {}",
                    ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
        }

        assertOrderOutbox(sagaId);
    }

    @Test
    void testDoublePaymentWithThreads() {
        String sagaId = UUID.randomUUID().toString();
        ExecutorService executor = null;

        try {
            executor = Executors.newFixedThreadPool(2);

            List<Callable<Object>> tasks = new ArrayList<>();

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    LOG.error("DataAccessException occurred from thread 1 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    LOG.error("DataAccessException occurred from thread 2 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            executor.invokeAll(tasks);
            assertOrderOutbox(sagaId);
        } catch (InterruptedException e) {
            LOG.error("Error calling complete payment!", e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    private void assertOrderOutbox(String sagaId) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository
                .findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME,
                        UUID.fromString(sagaId),
                        PaymentStatus.COMPLETED,
                        OutboxStatus.STARTED);

        assertTrue(orderOutboxEntity.isPresent());
        assertEquals(sagaId, orderOutboxEntity.get().getSagaId().toString());
    }

    private PaymentRequest getPaymentRequest(String sagaId) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerId(CUSTOMER_ID)
                .price(PRICE)
                .createdAt(Instant.now())
                .build();
    }
}
