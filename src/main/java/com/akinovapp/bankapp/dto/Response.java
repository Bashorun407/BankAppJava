package com.akinovapp.bankapp.dto;

import lombok.Builder;

@lombok.Data
@Builder
public class Response {
    private String responseCode;
    private String responseMessage;
    private Data data;
}
