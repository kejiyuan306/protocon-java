package com.example.protocon.core;

import java.net.InetSocketAddress;

import com.example.protocon.bo.RequestBo;
import com.example.protocon.bo.ResponseBo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class RawRequest {
    final ClientToken tk;
    final long clientId;
    final long gatewayId;
    final short cmdId;
    final RequestBo bo;
}

@AllArgsConstructor
class RawResponse {
    final ClientToken tk;
    final short cmdId;
    final ResponseBo bo;
}

@AllArgsConstructor
class RawSignInRequest {
    final ClientToken tk;
    final InetSocketAddress addr;
    final short cmdId;
    final long gatewayId;
    final long clientId;
}

@AllArgsConstructor
class RawSignInResponse {
    final ClientToken tk;
    final short cmdId;
    final byte status;
}

@AllArgsConstructor
class RawSignUpRequest {
    final ClientToken tk;
    final InetSocketAddress addr;
    final short cmdId;
    final long gatewayId;
}

@AllArgsConstructor
class RawSignUpResponse {
    final ClientToken tk;
    final short cmdId;
    final long clientId;
    final byte status;
}
