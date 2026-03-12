package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private static final String TOPIC = "order-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderController(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) throws JsonProcessingException {
        String orderJson = objectMapper.writeValueAsString(order);
        kafkaTemplate.send(TOPIC, order.getOrderId(), orderJson);
        log.info("OrderCreated event published to '{}': {}", TOPIC, orderJson);
        return ResponseEntity.ok("Order Created & Event Published: " + order.getOrderId());
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service is UP");
    }
}
