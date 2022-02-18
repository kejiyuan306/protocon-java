package com.example.protocon.config;

import com.example.protocon.service.GatewayService;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "protocon")
public class ProtoconAutoConfiguration {
    private int port = 8082;

    @Bean
    public GatewayService gateway() {
        GatewayService gateway = new GatewayService();
        gateway.setPort(port);
        return gateway;
    }
}
