package com.example.protocon.bo;

import java.net.InetSocketAddress;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientBo {
    private final long id;
    private final long gatewayId;
    private final InetSocketAddress addr;
}
