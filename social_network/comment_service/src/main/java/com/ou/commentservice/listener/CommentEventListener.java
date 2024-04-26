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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class CommentEventListener {
    @Autowired
    private KafkaTemplate<String, CommentEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(CommentEvent event) {
        log.info("Order Placed Event Received, Sending CommentEvent to {}: action {}, commentId {}, postId {}",
         event.getOrderService(), event.getAction(), event.getCommentId(), event.getPostId());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, CommentEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new CommentEvent(event.getOrderService(), event.getCommentId(), event.getPostId(), event.getAction()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}