package com.ou.postservice.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.ou.postservice.event.PostEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class PostEventListener {
    @Autowired
    private KafkaTemplate<String, PostEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(PostEvent event) {
        log.info("Order Placed Event Received, Sending PostEvent to {}: {}", event.getOrderService(), event.getOrderAction());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, PostEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new PostEvent(event.getOrderService(), event.getOrderAction(),
                        event.getPostId(), event.getRealtimeAction()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}