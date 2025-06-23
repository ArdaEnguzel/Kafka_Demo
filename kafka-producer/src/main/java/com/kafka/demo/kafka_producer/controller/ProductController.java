package com.kafka.demo.kafka_producer.controller;

import com.kafka.demo.kafka_producer.request.CreateProductRequest;
import com.kafka.demo.kafka_producer.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

  public final ProductService productService;

  @PostMapping
  public ResponseEntity<String> createProduct(
      @Valid @RequestBody CreateProductRequest createProductRequest) throws ExecutionException, InterruptedException {
    productService.createSyncProduct(createProductRequest);
    return ResponseEntity.ok("test");
  }
}
