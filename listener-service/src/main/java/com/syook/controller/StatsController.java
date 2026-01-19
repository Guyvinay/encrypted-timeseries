package com.syook.controller;

import com.syook.dto.BucketStatsDto;
import com.syook.dto.MessageRecordDto;
import com.syook.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats/latest")
    public ResponseEntity<BucketStatsDto> latestStats() {
        BucketStatsDto stats = statsService.getLatestBucketStats();
        return ResponseEntity.ok(stats); // 200 OK
    }

    @GetMapping("/records/latest")
    public ResponseEntity<List<MessageRecordDto>> latestRecords() {
        List<MessageRecordDto> records = statsService.getLatestRecords();
        return ResponseEntity.ok(records); // 200 OK
    }
}