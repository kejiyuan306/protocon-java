package com.example.protocon.service;

import lombok.Data;

@Data
public class GatewayService {
    private int port = 8082;

    public String getMessage() {
        return "port: " + port;
    }
}
