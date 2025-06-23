package com.kafka.demo.kafka_producer.service;

import com.kafka.demo.kafka_producer.event.ProductCreatedEvent;
import com.kafka.demo.kafka_producer.producer.ProductEventProducer;
import com.kafka.demo.kafka_producer.request.CreateProductRequest;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductEventProducer productEventProducer;

  public String createAsyncProduct(CreateProductRequest createProductRequest) {
    String productId = UUID.randomUUID().toString();
    ProductCreatedEvent productCreatedEvent =
        new ProductCreatedEvent(
            productId,
            createProductRequest.getName(),
            createProductRequest.getPrice(),
            createProductRequest.getQuantity());
    this.productEventProducer.sendAsyncProductCreatedEvent(productCreatedEvent);
    return null;
  }

  public String createSyncProduct(CreateProductRequest createProductRequest) throws ExecutionException, InterruptedException {
    String productId = UUID.randomUUID().toString();
    ProductCreatedEvent productCreatedEvent =
            new ProductCreatedEvent(
                    productId,
                    createProductRequest.getName(),
                    createProductRequest.getPrice(),
                    createProductRequest.getQuantity());
    this.productEventProducer.sendSyncProductCreatedEvent(productCreatedEvent);
    return null;
  }
}
