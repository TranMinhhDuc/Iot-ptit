package com.iot.backend.config;

import com.iot.backend.Handler.DashboardHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(getDashboardHandler(), "/dashboard").setAllowedOrigins("http://127.0.0.1:5500");
    }

    @Bean
    DashboardHandler getDashboardHandler(){
        return new DashboardHandler();
    }
}