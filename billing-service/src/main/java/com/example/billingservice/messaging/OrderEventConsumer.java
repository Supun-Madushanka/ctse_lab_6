package com.example.billingservice.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-topic", groupId = "billing-service-group")
    public void handleOrderCreated(String orderJson) {
        try {
            JsonNode order = objectMapper.readTree(orderJson);
            String orderId = order.path("orderId").asText();
            String item    = order.path("item").asText();
            int quantity   = order.path("quantity").asInt();

            String invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            log.info("[Billing] Received OrderCreated event -> orderId={}, item={}, quantity={}",
                    orderId, item, quantity);
            log.info("[Billing] Invoice generated: invoiceId={} for orderId={}", invoiceId, orderId);
        } catch (Exception e) {
            log.error("[Billing] Failed to process order event: {}", orderJson, e);
        }
    }
}
