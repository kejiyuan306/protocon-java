package com.example.protocon.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
    private final long time;
    private final short type;
    private final String data;
}
