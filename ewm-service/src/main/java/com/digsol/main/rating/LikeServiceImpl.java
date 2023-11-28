package com.digsol.main.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public Like addLike(Long userId, Long eventId, LikeType likeType) {

        log.debug("----- User {} added {} for event with Id {}", userId, likeType, eventId);

        return likeRepository.save(new Like(userId, eventId, likeType, LocalDateTime.now()));
    }

    @Override
    public void removeLike(Long userId, Long eventId, LikeType likeType) {

        Like like = likeRepository.findByUserIdAndEventIdAndType(userId, eventId, likeType)
                .orElseThrow(() -> new LikeNotFoundException("Like not found"));

        log.debug("----- User {} removed {} for event with Id {}", userId, likeType, eventId);

        likeRepository.delete(like);
    }

    @Override
    public Integer getRating(Long eventId) {

        log.debug("----- Rating for eventId {} provided", eventId);

        return likeRepository.getRating(eventId);
    }
}
