package com.example.protocon.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class SignInResponse {
    final ClientToken tk;
    final short cmdId;
    final byte status;
}
