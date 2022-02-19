package com.example.protocon.service;

import com.example.protocon.bo.RequestBo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Request {
    final ClientToken tk;
    final long clientId;
    final long gatewayId;
    final short cmdId;
    final RequestBo bo;
}
