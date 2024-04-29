package com.ou.postservice.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.ou.postservice.event.NotificationEvent;
import com.ou.postservice.event.PostMailEvent;
import com.ou.postservice.event.PostReactionEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class PostReactionEventListener {
    @Autowired
    private KafkaTemplate<String, PostReactionEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(PostReactionEvent event) {
        log.info("Order Placed Event Received, Sending PostReactionEvent to {} postId {} userId {}", event.getOrderService(), event.getPostId(), event.getUserId());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, PostReactionEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new PostReactionEvent(event.getOrderService(), event.getPostId(), event.getUserId()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}