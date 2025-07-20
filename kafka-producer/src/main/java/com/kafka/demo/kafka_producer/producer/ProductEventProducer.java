package com.kafka.demo.kafka_producer.producer;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.kafka.demo.avro.generated.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventProducer {
  private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

  @Value("${product.created.event.topic}")
  private String productCreatedEventTopic;

  public void sendAsyncProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
    CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
        kafkaTemplate.send(productCreatedEventTopic, productCreatedEvent);

    future.whenComplete(((stringProductCreatedEventSendResult, throwable) -> {
      if(throwable != null) {
        log.error(throwable.getLocalizedMessage());
      }
      else {
        log.info("success: " + stringProductCreatedEventSendResult.getRecordMetadata());
      }
    }));
    future.join(); // blocks the current thread until the future is complete.
  }

  public void sendSyncProductCreatedEvent(ProductCreatedEvent productCreatedEvent) throws ExecutionException, InterruptedException {
    SendResult<String, ProductCreatedEvent> result =
            kafkaTemplate.send(productCreatedEventTopic, productCreatedEvent).get();
    System.out.println(result.getRecordMetadata());
    System.out.println("arda");
  }

}
