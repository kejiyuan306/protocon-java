package com.example.protocon.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class MsgSender {
    final short apiVersion;
    AtomicBoolean connected;
    final Socket socket;
    final ClientToken tk;

    final ConcurrentLinkedQueue<Request> requestRx;
    final ConcurrentLinkedQueue<Response> responseRx;
    final ConcurrentLinkedQueue<SignUpResponse> signUpResponseRx;
    final ConcurrentLinkedQueue<SignInResponse> signInResponseRx;

    void send(Request r) {
        var bo = r.bo;
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeByte(0x00);
            os.writeShort(r.cmdId);
            os.writeLong(r.gatewayId);
            os.writeLong(r.clientId);
            os.writeLong(bo.getTime());
            os.writeShort(apiVersion);
            os.writeShort(bo.getType());
            os.writeInt(bo.getData().length());
            os.write(bo.getData().getBytes());

            log.info("发送请求，type：{}，data：{}", bo.getType(), bo.getData());
        } catch (SocketException e) {
            log.warn("发送请求失败，Socket 异常", e);
            connected.set(false);
        } catch (IOException e) {
            log.warn("发送请求失败，IO 异常", e);
        }
    }

    void send(Response r) {
        var bo = r.bo;
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeByte(0x80);
            os.writeShort(r.cmdId);
            os.writeLong(bo.getTime());
            os.writeByte(bo.getStatus());
            os.writeInt(bo.getData().length());
            os.write(bo.getData().getBytes());

            log.info("发送响应，status：{}，data：{}", bo.getStatus(), bo.getData());
        } catch (SocketException e) {
            log.warn("发送响应失败，Socket 异常", e);
            connected.set(false);
        } catch (IOException e) {
            log.warn("发送响应失败，IO 异常", e);
        }
    }

    void send(SignUpResponse r) {
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeByte(0x81);
            os.writeShort(r.cmdId);
            os.writeLong(r.clientId);
            os.writeByte(r.status);

            log.info("发送注册响应，clientId：{}，status：{}", r.clientId, r.status);
        } catch (SocketException e) {
            log.warn("发送注册响应失败，Socket 异常", e);
        } catch (IOException e) {
            log.warn("发送注册响应失败，IO 异常", e);
        }
    }

    void send(SignInResponse r) {
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeByte(0x82);
            os.writeShort(r.cmdId);
            os.writeByte(r.status);

            log.info("发送登录响应，status：{}", r.status);
        } catch (SocketException e) {
            log.warn("发送登录响应失败，Socket 异常", e);
        } catch (IOException e) {
            log.warn("发送登录响应失败，IO 异常", e);
        }
    }

    void poll() {
        var request = requestRx.poll();
        if (request != null) {
            send(request);
        }

        var response = responseRx.poll();
        if (response != null) {
            send(response);
        }

        var signUpResponse = signUpResponseRx.poll();
        if (signUpResponse != null) {
            send(signUpResponse);
        }

        var signInResponse = signInResponseRx.poll();
        if (signInResponse != null) {
            send(signInResponse);
        }
    }

    void run() {
        // 发送请求
        Thread handleSendMessagesThread =
                new Thread(
                        () -> {
                            while (connected.get()) {
                                poll();

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    log.warn("Unexpected interrupt, ", e);
                                    Thread.currentThread().interrupt();
                                }
                            }

                            log.info("Client连接断开，client token：{}，Sender 关闭", tk.value);
                        });
        handleSendMessagesThread.setDaemon(true);
        handleSendMessagesThread.start();
    }
}
