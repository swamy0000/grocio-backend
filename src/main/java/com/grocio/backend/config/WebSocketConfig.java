package com.grocio.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 🟢 /topic తో స్టార్ట్ అయ్యే ఛానల్స్ కి సర్వర్ మెసేజ్ పుష్ చేస్తుంది
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 🟢 ఫ్లట్టర్ క్లయింట్ కనెక్ట్ అవ్వడానికి ఎండ్ పాయింట్ URL:
        // ws://localhost:8089/ws
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");
    }
}