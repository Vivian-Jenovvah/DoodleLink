package com.collaborativecanvas.server.repository;
import com.collaborativecanvas.server.entity.CanvasSnapshotEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface CanvasSnapshotRepository extends JpaRepository<CanvasSnapshotEntity, UUID> {
 @Query("select s from CanvasSnapshotEntity s where s.roomId=:roomId and s.snapshotSequenceNumber <= :sequence order by s.snapshotSequenceNumber desc") List<CanvasSnapshotEntity> latestAtOrBefore(@Param("roomId") UUID roomId, @Param("sequence") long sequence, org.springframework.data.domain.Pageable page);
 List<CanvasSnapshotEntity> findByRoomIdOrderBySnapshotSequenceNumberDesc(UUID roomId);
 void deleteByRoomId(UUID roomId);
}
