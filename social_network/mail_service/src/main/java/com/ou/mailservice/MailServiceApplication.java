package com.ou.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ou.mailservice.pojo.OrderPlacedEvent;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MailServiceApplication {
	@Autowired
	private ObservationRegistry observationRegistry;

    public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}

	@KafkaListener(topics = "mailTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent.getOrderAction());
        });
    }
}