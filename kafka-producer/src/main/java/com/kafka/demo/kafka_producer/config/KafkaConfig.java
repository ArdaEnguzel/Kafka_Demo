package com.kafka.demo.kafka_producer.config;

import java.util.HashMap;
import java.util.Map;

import com.kafka.demo.avro.generated.ProductCreatedEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.producer.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  @Value("${spring.kafka.producer.ack}")
  private String ack;

  @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
  private String deliveryTimeout;


  @Value("${spring.kafka.producer.properties.request.timeout.ms}")
  private String requestTimeout;
  @Value(("${spring.kafka.producer.properties.enable.idempotence}"))
  private Boolean enableIdempotence;

  Map<String, Object> producerConfigs() {
    Map<String, Object> config = new HashMap<>();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
    config.put(ProducerConfig.ACKS_CONFIG, ack);
    config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
    config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
    config.put("schema.registry.url", "http://localhost:7055");
    config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);

    return config;
  }

  @Bean
  ProducerFactory<String, ProductCreatedEvent> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  KafkaTemplate<String, ProductCreatedEvent> kafkaProductCreatedTemplate() {
    return new KafkaTemplate<String, ProductCreatedEvent>(producerFactory());
  }


  @Bean
  NewTopic createTopic() {
    return TopicBuilder.name("product-created-events-topic")
            .partitions(3)
            .replicas(3)
            .configs(Map.of("min.insync.replicas","2"))
            .build();
  }

}
