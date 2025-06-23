package com.kafka.demo.kafka_producer.event;

import java.math.BigDecimal;

public record ProductCreatedEvent(
        String productId,
        String name,
        BigDecimal price,
        Integer quantity
) {}
