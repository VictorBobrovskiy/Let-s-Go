package com.digsol.main.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Collection<Request> findAllByRequesterId(Long requesterId);

    Collection<Request> findAllByRequesterIdAndEventId(Long requesterId, Long eventId);

    Collection<Request> findAllByEventId(Long eventId);

    Collection<Request> findAllByIdIn(Collection<Long> ids);

    @Query("select new com.digsol.main.request.RequestCount(req.event.id, count(req.id)) " +
            "from Request as req " +
            "where req.event.id in :eventIds " +
            "and req.status = 'CONFIRMED' " +
            "group by req.event.id")
    Collection<RequestCount> getConfirmedRequests(Collection<Long> eventIds);
}