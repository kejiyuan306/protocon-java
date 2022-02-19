package com.example.protocon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.protocon.service.GatewayService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GatewayServiceTests {
    @Autowired
    GatewayService gateway;

    @Test
    void testMessage() {
        assertEquals("port: 8083", gateway.getMessage());
    }
}
