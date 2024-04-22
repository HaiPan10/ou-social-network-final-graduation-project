package com.ou.mailservice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ou.mailservice.event.AccountMailEvent;
import com.ou.mailservice.pojo.Account;
import com.ou.mailservice.event.OrderPlacedEvent;
import com.ou.mailservice.service.interfaces.MailService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MailServiceApplication {
	@Autowired
	private ObservationRegistry observationRegistry;
    @Autowired
    private MailService mailService;

    public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}

	@KafkaListener(topics = "mailAccountTopic")
    public void handleNotification(AccountMailEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}, {} {} {} {} {} {}>",
            orderPlacedEvent.getOrderAction(), orderPlacedEvent.getId(), orderPlacedEvent.getEmail(), orderPlacedEvent.getFirstName(),
            orderPlacedEvent.getLastName(), orderPlacedEvent.getVerificationCode(), orderPlacedEvent.getStatus());
            try {
                Account account = new Account(orderPlacedEvent.getId(), orderPlacedEvent.getEmail(),
                    orderPlacedEvent.getVerificationCode(), orderPlacedEvent.getStatus(),
                    orderPlacedEvent.getFirstName(), orderPlacedEvent.getLastName());
                Method method = MailService.class.getMethod(orderPlacedEvent.getOrderAction(), Account.class);
                method.invoke(mailService, account);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @KafkaListener(topics = "mailTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent.getOrderAction());
        });
    }
}