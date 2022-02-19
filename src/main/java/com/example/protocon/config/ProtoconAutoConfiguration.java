package com.example.protocon.config;

import com.example.protocon.service.GatewayService;
import com.example.protocon.service.GatewayServiceImpl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "protocon")
public class ProtoconAutoConfiguration {
    private int port = 8082;

    @Bean
    public GatewayService gateway() {
        var gateway = new GatewayServiceImpl();
        gateway.setListenPort(port);
        return gateway;
    }
}
