package com.ou.realtimeservice.configs;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.ou.realtimeservice.pojo.SocketClient;
import com.ou.realtimeservice.service.interfaces.SocketService;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired
	private SocketService socketService;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/comment", "/reaction", "/comment-total", "/home", "/profile", "/reply");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint("/api/ws")
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) throws InterruptedException, ExecutionException {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		Long userId = Long.parseLong(headerAccessor.getFirstNativeHeader("user"));
		SocketClient socketClient = new SocketClient(headerAccessor.getSessionId(), userId);
		socketService.addOnlineClient(socketClient);
	}

	@EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		Long userId = Long.parseLong(headerAccessor.getFirstNativeHeader("user"));
		System.out.println("ws headerAccessor " + headerAccessor.toString());
		SocketClient socketClient = new SocketClient(headerAccessor.getSessionId(), userId);
		socketService.addSocketClient(headerAccessor.getDestination(), socketClient);

		Pattern reactionPath = Pattern.compile("/reaction/(.*)");
		Matcher matcherReaction = reactionPath.matcher(headerAccessor.getDestination());

		Pattern commentTotalPath = Pattern.compile("/comment-total/(.*)");
		Matcher matcherCommentTotal = commentTotalPath.matcher(headerAccessor.getDestination());

		if (matcherReaction.matches()) {
			socketService.realtimePostReaction(Long.parseLong(matcherReaction.group(1)), Long.valueOf(0));
		} else if (matcherCommentTotal.matches()) {
			socketService.realtimeCommentTotal(Long.parseLong(matcherCommentTotal.group(1)));
		}
    }

	@EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String path = headerAccessor.getFirstNativeHeader("id");
		socketService.removeSocketClient(path, headerAccessor.getSessionId());
    }

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws InterruptedException, ExecutionException {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		socketService.removeOfflineClient(sessionId);
	}
}
