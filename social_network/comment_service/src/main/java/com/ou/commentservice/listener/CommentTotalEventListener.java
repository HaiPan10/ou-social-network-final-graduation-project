package com.ou.commentservice.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.ou.commentservice.event.CommentEvent;
import com.ou.commentservice.event.CommentTotalEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class CommentTotalEventListener {
    @Autowired
    private KafkaTemplate<String, CommentTotalEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(CommentTotalEvent event) {
        log.info("Order Placed Event Received, Sending CommentTotalEvent to {}: postId {}",
         event.getOrderService(), event.getPostId());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, CommentTotalEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new CommentTotalEvent(event.getOrderService(), event.getPostId()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}