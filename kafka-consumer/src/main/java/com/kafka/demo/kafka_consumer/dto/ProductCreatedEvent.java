package com.kafka.demo.kafka_consumer.dto;


import java.math.BigDecimal;

public record ProductCreatedEvent(
        String productId,
        String name,
        BigDecimal price,
        Integer quantity
) {}

