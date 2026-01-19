package com.syook.dto;

import lombok.Data;

@Data
public class MessagePayload {

    private String name;
    private String origin;
    private String destination;
    private String secret_key;
}