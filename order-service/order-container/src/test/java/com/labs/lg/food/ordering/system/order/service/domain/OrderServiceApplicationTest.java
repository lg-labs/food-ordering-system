package com.labs.lg.food.ordering.system.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class OrderServiceApplicationTest {

    @BeforeEach
    void setUp() {
        log.info("Start testing");
    }

    @Test
    void test() {
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        log.info("End testing");
    }
}