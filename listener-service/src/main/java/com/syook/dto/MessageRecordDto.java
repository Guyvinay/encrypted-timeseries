package com.syook.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MessageRecordDto {
    private String name;
    private String origin;
    private String destination;
    private Instant timestamp;
}