package com.example.protocon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("protocon.port=8082")
public class GatewayServiceTests {
    @Autowired
    GatewayService service;

    @Test
    void testMessage() {
        assertEquals("port: 8082", service.getMessage());
    }
}
