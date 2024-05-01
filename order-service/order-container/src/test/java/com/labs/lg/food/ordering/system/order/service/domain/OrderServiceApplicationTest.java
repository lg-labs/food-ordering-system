package com.labs.lg.food.ordering.system.order.service.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceApplicationTest {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceApplicationTest.class);

    @BeforeEach
    void setUp() {
        LOG.info("Start testing");
    }

    @Test
    void test() {
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        LOG.info("End testing");
    }
}