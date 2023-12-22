package com.digsol.main.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value =
            "SELECT * FROM likes WHERE user_id = :userId AND event_id = :eventId",
            nativeQuery = true)
    Optional<Like> findByUserIdAndEventId(Long userId, Long eventId);

    Optional<Like> findByUserIdAndEventIdAndType(Long userId, Long eventId, LikeType likeType);

    @Query(value = "SELECT" +
            "    event_id," +
            "    SUM(CASE WHEN type = 'LIKE' THEN 1 ELSE 0 END) -" +
            "    SUM(CASE WHEN type = 'DISLIKE' THEN 1 ELSE 0 END) AS rating" +
            "    FROM likes" +
            "    GROUP BY event_id;",
            nativeQuery = true)
    Integer getRating(Long eventId);
}