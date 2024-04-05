package com.labs.lg.food.ordering.system.application.handler;

import lombok.Builder;

@Builder
public record ErrorDTO(String code, String message) {
}
