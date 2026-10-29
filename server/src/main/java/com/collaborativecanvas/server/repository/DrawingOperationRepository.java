package com.collaborativecanvas.server.repository;
import com.collaborativecanvas.server.entity.DrawingOperationEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface DrawingOperationRepository extends JpaRepository<DrawingOperationEntity, UUID> {
 Optional<DrawingOperationEntity> findByRoomIdAndOperationId(UUID roomId, UUID operationId);
 @Query("select o from DrawingOperationEntity o where o.roomId=:roomId and o.sequenceNumber>:after and o.sequenceNumber<=:through order by o.sequenceNumber") List<DrawingOperationEntity> replay(@Param("roomId") UUID roomId, @Param("after") long after, @Param("through") long through);
 void deleteByRoomId(UUID roomId);
}
