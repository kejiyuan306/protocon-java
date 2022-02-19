package com.example.protocon.service;

import com.example.protocon.bo.ClientBo;
import com.example.protocon.bo.RequestBo;
import com.example.protocon.bo.ResponseBo;
import java.net.SocketAddress;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface GatewayService {
    // TODO: We need persistence here, such as SQLite
    void init(List<ClientBo> clients);

    List<SocketAddress> getAllAddrs();

    void registerRequestHandler(short type, BiFunction<ClientBo, RequestBo, ResponseBo> function);

    void registerSignUpHandler(Consumer<ClientBo> consumer);

    void registerSignInHandler(Consumer<ClientBo> consumer);

    void send(long clientId, RequestBo request, Consumer<ResponseBo> consumer);
}
