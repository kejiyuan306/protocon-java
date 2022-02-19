package com.example.protocon.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBo {
    private final long time;
    private final byte status;
    private final String data;
}
