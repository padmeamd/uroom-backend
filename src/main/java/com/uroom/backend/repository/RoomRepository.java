package com.uroom.backend.repository;

import com.uroom.backend.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("""
        select distinct r
        from Room r
        join r.tags t
        where t = :tag
    """)
    List<Room> findByTag(@Param("tag") String tag);
}