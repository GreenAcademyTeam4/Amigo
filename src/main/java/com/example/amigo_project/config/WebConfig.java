package com.example.amigo_project.config;

import com.example.amigo_project.handler.AlarmHandler;
import com.example.amigo_project.handler.ChatHandler;
import com.example.amigo_project.handler.SignalingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;
    private final SignalingHandler signalingHandler;
    private final SocketInterceptor socketInterceptor;
    private final AlarmHandler alarmHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/chat").addInterceptors(new HttpSessionHandshakeInterceptor(),socketInterceptor).setAllowedOrigins("*");
        registry.addHandler(signalingHandler,"/signaling" ).addInterceptors(socketInterceptor).setAllowedOrigins("*");
        registry.addHandler(alarmHandler,"/alarm").addInterceptors(socketInterceptor).setAllowedOrigins("*");
    }
}
