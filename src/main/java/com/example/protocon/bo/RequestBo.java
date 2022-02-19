package com.example.protocon.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestBo {
    private final long time;
    private final short type;
    private final String data;
}
