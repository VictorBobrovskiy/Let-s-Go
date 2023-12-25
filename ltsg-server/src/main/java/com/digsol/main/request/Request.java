package com.digsol.main.request;

import com.digsol.main.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.digsol.main.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime created;

    public Request(User requester, Event event, RequestStatus status) {
        this.requester = requester;
        this.event = event;
        this.status = status;
    }
}