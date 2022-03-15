package com.example.protocon.config;

import com.example.protocon.core.Gateway;

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
    public Gateway gateway() {
        var gateway = new Gateway();
        gateway.setListenPort(port);
        return gateway;
    }
}
