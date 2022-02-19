package com.example.protocon.service;

import com.example.protocon.bo.ResponseBo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class Response {
    final ClientToken tk;
    final short cmdId;
    final ResponseBo bo;
}
