package com.example.protocon.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class SignUpResponse {
    final ClientToken tk;
    final short cmdId;
    final long clientId;
    final byte status;
}
