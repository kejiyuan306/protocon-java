package com.example.protocon.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class MsgReceiver {
    private static final int BUFFER_SIZE = 1 << 15;

    final short apiVersion;
    AtomicBoolean connected;
    final Socket socket;
    final ClientToken tk;
    InetSocketAddress addr;

    final ConcurrentLinkedQueue<RawRequest> requestTx;
    final ConcurrentLinkedQueue<RawResponse> responseTx;
    final ConcurrentLinkedQueue<RawSignUpRequest> signUpRequestTx;
    final ConcurrentLinkedQueue<RawSignInRequest> signInRequestTx;

    final ConcurrentLinkedQueue<ClientToken> disconnectionTx;

    public void run() {
        Thread handleReceiveMessagesThread =
                new Thread(
                        () -> {
                            byte[] buffer = new byte[BUFFER_SIZE];
                            while (connected.get()) {
                                // 接收请求
                                if (!receive(buffer)) break;

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    log.warn("Interrupted, ", e);
                                    Thread.currentThread().interrupt();
                                }
                            }

                            log.info("Client连接断开，{}，Receiver 关闭", addr);

                            connected.set(false);

                            disconnectionTx.add(tk);
                        });
        handleReceiveMessagesThread.setDaemon(true);
        handleReceiveMessagesThread.start();
    }

    private boolean readExact(InputStream stream, byte[] buf, int length) {
        int offset = 0;
        while (offset < length) {
            try {
                int len = stream.read(buf, offset, length - offset);
                if (len == -1) return false;
                offset += len;
            } catch (SocketTimeoutException e) {
                // 非阻塞式 read，不需要处理超时
            } catch (IOException e) {
                log.info("读取客户端请求失败", e);
                return false;
            }
        }
        return true;
    }

    private boolean receiveRequest(InputStream is, short cmdId, byte[] buf) {
        if (!readExact(is, buf, Long.BYTES)) return false;
        long gatewayId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        if (!readExact(is, buf, Long.BYTES)) return false;
        long clientId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        if (!readExact(is, buf, Long.BYTES)) return false;
        long time = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        if (!readExact(is, buf, Short.BYTES)) return false;
        short deviceApiVersion = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getShort();

        if (!readExact(is, buf, Short.BYTES)) return false;
        short type = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getShort();

        if (!readExact(is, buf, Integer.BYTES)) return false;
        int length = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getInt();

        if (!readExact(is, buf, length)) return false;
        String data = new String(buf, 0, length);

        if (apiVersion != deviceApiVersion) {
            log.warn("Api 版本不一致，期望：{}，实际：{}", apiVersion, deviceApiVersion);
            return false;
        }

        requestTx.add(new RawRequest(tk, clientId, gatewayId, cmdId, new Request(time, type, data)));

        log.info("收到请求，client token：{}，type：{}，value：{}", tk.value, type, data);

        return true;
    }

    private boolean receiveResponse(InputStream is, short cmdId, byte[] buf) {
        if (!readExact(is, buf, Long.BYTES)) return false;
        long time = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        if (!readExact(is, buf, Byte.BYTES)) return false;
        byte status = ByteBuffer.wrap(buf).get();

        if (!readExact(is, buf, Integer.BYTES)) return false;
        int length = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getInt();

        if (!readExact(is, buf, length)) return false;
        String data = new String(buf, 0, length);

        responseTx.add(new RawResponse(tk, cmdId, new Response(time, status, data)));

        log.info("收到响应，client token：{}，status：{}，value：{}", tk.value, status, data);

        return true;
    }

    private boolean receiveSignUpRequest(InputStream is, short cmdId, byte[] buf) {
        if (!readExact(is, buf, Long.BYTES)) return false;
        long gatewayId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        signUpRequestTx.add(new RawSignUpRequest(tk, addr, cmdId, gatewayId));

        log.info("收到注册请求，client token：{}，gateway ID：{}", tk.value, gatewayId);

        return true;
    }

    private boolean receiveSignInRequest(InputStream is, short cmdId, byte[] buf) {
        if (!readExact(is, buf, Long.BYTES)) return false;
        long gatewayId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        if (!readExact(is, buf, Long.BYTES)) return false;
        long clientId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getLong();

        signInRequestTx.add(new RawSignInRequest(tk, addr, cmdId, gatewayId, clientId));

        log.info("收到登录请求，gateway ID：{}，client ID：{}", gatewayId, clientId);

        return true;
    }

    private boolean receive(byte[] buf) {
        try {
            InputStream is = socket.getInputStream();

            if (!readExact(is, buf, Byte.BYTES)) return false;
            byte cmdFlag = ByteBuffer.wrap(buf).get();

            if (!readExact(is, buf, Short.BYTES)) return false;
            short cmdId = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).getShort();

            if (cmdFlag == 0x00) return receiveRequest(is, cmdId, buf);
            else if (cmdFlag == 0x80) return receiveResponse(is, cmdId, buf);
            else if (cmdFlag == 0x01) return receiveSignUpRequest(is, cmdId, buf);
            else if (cmdFlag == 0x02) return receiveSignInRequest(is, cmdId, buf);
        } catch (IOException e) {
            log.error("创建 InputStream 失败", e);
            return false;
        }
        return false;
    }
}
