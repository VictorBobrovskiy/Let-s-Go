package com.digsol.main.rating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LikeType type;

    @PastOrPresent
    @Column(name = "created")
    private LocalDateTime created;

    public Like(Long userId, Long eventId, LikeType type, LocalDateTime created) {
        this.userId = userId;
        this.eventId = eventId;
        this.type = type;
        this.created = created;
    }
}
