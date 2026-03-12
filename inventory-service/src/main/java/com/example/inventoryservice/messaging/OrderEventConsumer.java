package com.example.inventoryservice.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-topic", groupId = "inventory-service-group")
    public void handleOrderCreated(String orderJson) {
        try {
            JsonNode order = objectMapper.readTree(orderJson);
            String orderId = order.path("orderId").asText();
            String item    = order.path("item").asText();
            int quantity   = order.path("quantity").asInt();

            log.info("[Inventory] Received OrderCreated event -> orderId={}, item={}, quantity={}",
                    orderId, item, quantity);
            log.info("[Inventory] Stock updated: reserved {} unit(s) of '{}'", quantity, item);
        } catch (Exception e) {
            log.error("[Inventory] Failed to process order event: {}", orderJson, e);
        }
    }
}
