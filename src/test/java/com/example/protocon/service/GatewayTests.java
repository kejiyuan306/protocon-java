package com.example.protocon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import com.example.protocon.core.Gateway;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class GatewayTests {
    @Autowired
    Gateway gateway;

    @BeforeAll
    void initAll() {
        gateway.init(new ArrayList<>());
    }

    @Test
    void testGetAllAddrs() {
        assertEquals(0, gateway.getAllAddrs().size());
    }
}
