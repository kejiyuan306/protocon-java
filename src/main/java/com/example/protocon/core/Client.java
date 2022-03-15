package com.example.protocon.core;

import java.net.InetSocketAddress;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {
    private final long id;
    private final long gatewayId;
    private final InetSocketAddress addr;
}
