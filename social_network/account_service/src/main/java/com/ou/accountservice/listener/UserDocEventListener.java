package com.ou.accountservice.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.ou.accountservice.event.UserDocEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class UserDocEventListener {
    @Autowired
    private KafkaTemplate<String, UserDocEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(UserDocEvent event) {
        log.info("Order Placed Event Received, Sending UserDocEvent to {}: {}", event.getOrderService(), event.getUserId(),
         event.getDisplayName(), event.getPhotoUrl(), event.getActiveStatus());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, UserDocEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new UserDocEvent(event.getOrderService(), event.getUserId(),
                         event.getDisplayName(), event.getPhotoUrl(), event.getActiveStatus()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}