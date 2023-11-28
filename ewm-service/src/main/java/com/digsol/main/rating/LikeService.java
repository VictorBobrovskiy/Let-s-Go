package com.digsol.main.rating;

public interface LikeService {

    Like addLike(Long userId, Long eventId, LikeType likeType);

    void removeLike(Long userId, Long eventId, LikeType likeType);

    Integer getRating(Long eventId);
}
