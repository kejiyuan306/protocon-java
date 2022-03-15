package com.example.protocon.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private final long time;
    private final byte status;
    private final String data;
}
