package com.example.protocon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(GatewayServiceProperties.class)
public class GatewayServiceImpl implements GatewayService {
    @Autowired
    private GatewayServiceProperties properties;

    @Override
    public String getMessage() {
        return "port: " + properties.getPort();
    }
}
