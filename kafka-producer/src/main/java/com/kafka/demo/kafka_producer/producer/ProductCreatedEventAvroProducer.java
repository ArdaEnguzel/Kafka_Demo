package com.kafka.demo.kafka_producer.producer;

import avro.generated.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCreatedEventAvroProducer {
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Value("${product.created.event.topic}")
    private String productCreatedEventTopic;

    public void sendAvroProductCreatedEvent(ProductCreatedEvent productCreatedEvent) throws ExecutionException, InterruptedException {
        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(productCreatedEventTopic, productCreatedEvent).get();
        System.out.println(result.getRecordMetadata());
    }
}