package com.uroom.backend.repository;

import com.uroom.backend.domain.Room;
import com.uroom.backend.domain.Room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
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

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByCreatorId(UUID creatorId);

    @Query("""
        select r from Room r
        where r.dateTime <= :endDate
        and r.dateTime >= :startDate
    """)
    List<Room> findByDateTimeBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    List<Room> findByUniversity(String university);
}
