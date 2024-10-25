package com.example.amigo_project.config;

import com.example.amigo_project.handler.ChatHandler;
import com.example.amigo_project.handler.SignalingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;
    private final SignalingHandler signalingHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/chat").setAllowedOrigins("*");
        registry.addHandler(signalingHandler,"/signaling" ).setAllowedOrigins("*");
    }
}
