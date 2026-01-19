package com.syook.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "message_bucket")
@Data
public class MessageBucketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bucketTime", unique = true)
    private Instant bucketTime;

    @Column(name = "records", columnDefinition = "TEXT")
    private String records;


    private int totalCount;

    private int validCount;
}