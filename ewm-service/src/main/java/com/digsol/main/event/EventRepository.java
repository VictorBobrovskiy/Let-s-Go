package com.digsol.main.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.Collection;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long ownerId, Pageable pageable);

    Collection<Event> findAllByCategoryId(Long categoryId);

    @Query("select ev " +
            "from Event as ev " +
            "where (:users is null or ev.initiator.id in :users) " +
            "and (:states is null or ev.state in :states) " +
            "and (:categories is null or ev.category.id in :categories) " +
            "and (coalesce(:start, null) is null or ev.eventDate > :start) " +
            "and (coalesce(:end, null) is null or ev.eventDate < :end)")
    Page<Event> findAllOrderByEventDate(Collection<Long> users,
                                        Collection<EventState> states,
                                        Collection<Long> categories,
                                        LocalDateTime start,
                                        LocalDateTime end,
                                        Pageable pageable);

    @Query("select ev from " +
            "Event as ev " +
            "where (:text is null) or ((lower(ev.annotation) like %:text%) or (lower(ev.description) like %:text%)) " +
            "and (:categories is null or ev.category.id in :categories) " +
            "and (:paid is null or ev.paid = :paid) " +
            "and (ev.eventDate is null or ev.eventDate is not null) " +
            "and (false = :onlyAvailable or (true = :onlyAvailable and ev.participantLimit > 0)) " +
            "and (coalesce(:start, null) is null or ev.eventDate > :start) " +
            "and (coalesce(:end, null) is null or ev.eventDate < :end)")
    Page<Event> findAllOrderByEventDate(String text,
                                        Collection<Long> categories,
                                        Boolean paid,
                                        Boolean onlyAvailable,
                                        LocalDateTime start,
                                        LocalDateTime end,
                                        Pageable pageable);

    Page<Event> findAllEventsByLocationId(Long locationId, Pageable pageable);

    @Query(value = "SELECT e.* " +
            "FROM events e " +
            "JOIN locations l ON e.location_id = l.id " +
            "WHERE distance(l.lat, l.lon, :userLat, :userLon) <= :distance " +
            "ORDER BY distance(l.lat, l.lon, :userLat, :userLon)",
            nativeQuery = true)
    @QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "0"))
    Page<Event> findAllByDistanceOrderByDistance(Double userLat, Double userLon, Double distance, Pageable pageable);

    @Query(value = "SELECT e.*, distance(l.lat, l.lon, :userLat, :userLon) " +
            "FROM events e " +
            "JOIN locations l ON e.location_id = l.id " +
            "ORDER BY distance(l.lat, l.lon, :userLat, :userLon)",
            nativeQuery = true)

    @QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "0"))
    Page<Event> findAllOrderByDistance(Double userLat, Double userLon, Pageable pageable);

    @Query("select ev from " +
            "Event as ev " +
            "where (:text is null) or ((lower(ev.annotation) like %:text%) or (lower(ev.description) like %:text%)) " +
            "and (:categories is null or ev.category.id in :categories) " +
            "and (:paid is null or ev.paid = :paid) " +
            "and (ev.eventDate is null or ev.eventDate is not null) " +
            "and (false = :onlyAvailable or (true = :onlyAvailable and ev.participantLimit > 0)) " +
            "and (coalesce(:start, null) is null or ev.eventDate > :start) " +
            "and (coalesce(:end, null) is null or ev.eventDate < :end)")
    Page<Event> findAllOrderById(String text,
                                 Collection<Long> categories,
                                 Boolean paid,
                                 Boolean onlyAvailable,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 Pageable pageable);
}