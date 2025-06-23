package com.kafka.demo.kafka_producer.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
  @NotNull private String name;
  @NotNull private BigDecimal price;
  @NotNull private Integer quantity;
}
