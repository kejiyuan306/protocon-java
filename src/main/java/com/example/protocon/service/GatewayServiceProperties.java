package com.example.protocon.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("protocon")
public class GatewayServiceProperties {
    private int port;
}
