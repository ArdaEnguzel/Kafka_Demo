package com.kafka.demo.kafka_consumer.listener;

import com.kafka.demo.kafka_consumer.dto.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@KafkaListener(topics = "product-created-events-topic")
@Slf4j
public class ProductCreatedEventListener {
    @KafkaHandler()
    public void handleProductCreatedEvent(Map<String, Object> productCreatedEvent) {
        log.info("Received" +  productCreatedEvent.toString());
    }
}
