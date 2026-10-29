package com.collaborativecanvas.server.config;

import com.collaborativecanvas.shared.protocol.WebSocketDestinations;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Override public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic", "/queue");
    registry.setApplicationDestinationPrefixes(WebSocketDestinations.APPLICATION_PREFIX);
    registry.setUserDestinationPrefix("/user");
  }
  @Override public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(WebSocketDestinations.ENDPOINT).setAllowedOriginPatterns("http://localhost:*", "https://localhost:*", "https://*");
  }
}
