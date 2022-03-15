package com.example.protocon.service;

import java.net.InetSocketAddress;

import com.example.protocon.bo.RequestBo;
import com.example.protocon.bo.ResponseBo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Request {
    final ClientToken tk;
    final long clientId;
    final long gatewayId;
    final short cmdId;
    final RequestBo bo;
}

@AllArgsConstructor
class Response {
    final ClientToken tk;
    final short cmdId;
    final ResponseBo bo;
}

@AllArgsConstructor
class SignInRequest {
    final ClientToken tk;
    final InetSocketAddress addr;
    final short cmdId;
    final long gatewayId;
    final long clientId;
}

@AllArgsConstructor
class SignInResponse {
    final ClientToken tk;
    final short cmdId;
    final byte status;
}

@AllArgsConstructor
class SignUpRequest {
    final ClientToken tk;
    final InetSocketAddress addr;
    final short cmdId;
    final long gatewayId;
}

@AllArgsConstructor
class SignUpResponse {
    final ClientToken tk;
    final short cmdId;
    final long clientId;
    final byte status;
}
