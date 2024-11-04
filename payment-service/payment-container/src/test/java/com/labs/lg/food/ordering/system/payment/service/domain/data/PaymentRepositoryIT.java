package com.labs.lg.food.ordering.system.payment.service.domain.data;

import com.labs.lg.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.labs.lg.food.ordering.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import com.labs.lg.food.ordering.system.payment.service.domain.boot.Bootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryIT extends Bootstrap {

    @Autowired
    PaymentJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void test() {
        List<PaymentEntity> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

}
