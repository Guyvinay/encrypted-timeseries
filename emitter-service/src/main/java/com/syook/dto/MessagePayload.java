package com.syook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {

    private String name;
    private String origin;
    private String destination;
    private String secret_key;
}
