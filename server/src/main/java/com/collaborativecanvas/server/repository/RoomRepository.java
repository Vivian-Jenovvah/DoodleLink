package com.collaborativecanvas.server.repository;
import com.collaborativecanvas.server.entity.RoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.*;
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
 Optional<RoomEntity> findByRoomCode(String roomCode);
 boolean existsByRoomCode(String roomCode);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select r from RoomEntity r where r.roomCode=:code") Optional<RoomEntity> findForUpdate(@Param("code") String code);
 List<RoomEntity> findByStatusAndExpiresAtBefore(String status, Instant now);
}
