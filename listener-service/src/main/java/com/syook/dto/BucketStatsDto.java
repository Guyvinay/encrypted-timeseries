package com.syook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BucketStatsDto {
    private Instant bucketTime;
    private int total;
    private int valid;
    private double successRate;
}