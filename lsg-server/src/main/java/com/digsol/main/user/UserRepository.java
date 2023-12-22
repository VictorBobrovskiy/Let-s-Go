package com.digsol.main.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select us " +
            "from User as us " +
            "where (:ids is null or us.id in :ids)")
    Page<User> getUsers(Collection<Long> ids, Pageable pageable);
}