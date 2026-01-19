package com.syook.repo;

import com.syook.entity.MessageBucketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface MessageBucketRepository extends JpaRepository<MessageBucketEntity, Long> {

    @Query("""
       SELECT b FROM MessageBucketEntity b
       WHERE b.bucketTime = :bucketTime
    """)
    Optional<MessageBucketEntity> findBucketForUpdate(
            @Param("bucketTime") Instant bucketTime
    );
}