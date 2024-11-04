package com.labs.lg.food.ordering.system.order.service.domain.api.rest;

import com.labs.lg.food.ordering.system.order.service.domain.boot.Bootstrap;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class HealthCheckIT extends Bootstrap {

    @Test
    void it_should_be_healthy() {
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
}