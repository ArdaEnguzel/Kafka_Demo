package com.kafka.demo.kafka_producer.service;

import com.kafka.demo.avro.generated.ProductCreatedEvent;
import com.kafka.demo.kafka_producer.producer.ProductCreatedEventAvroProducer;
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
  private final ProductCreatedEventAvroProducer productCreatedEventAvroProducer;

  public String createAvroProduct(CreateProductRequest createProductRequest) throws ExecutionException, InterruptedException {
    String productId = UUID.randomUUID().toString();
    ProductCreatedEvent productCreatedEvent =
            new ProductCreatedEvent(
                    productId,
                    createProductRequest.getName(),
                    createProductRequest.getPrice().floatValue(),
                    createProductRequest.getQuantity());
    this.productCreatedEventAvroProducer.sendAvroProductCreatedEvent(productCreatedEvent);
    return null;
  }
}
