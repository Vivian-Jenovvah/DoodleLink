package com.collaborativecanvas.server.service;
import com.collaborativecanvas.server.repository.RoomRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
@Service public class RoomCleanupService { private final RoomRepository rooms; private final ParticipantService participants;
 @Value("${canvas.room.retention-grace-days:7}") long retentionGraceDays;
 public RoomCleanupService(RoomRepository rooms,ParticipantService participants){this.rooms=rooms;this.participants=participants;}
 @Scheduled(fixedDelayString="${canvas.room.cleanup-millis:3600000}") @Transactional public void expire(){Instant now=Instant.now();for(var r:rooms.findByStatusAndExpiresAtBefore("ACTIVE", now))if(participants.count(r.roomCode)==0){r.status="EXPIRED";rooms.save(r);}Instant purgeBefore=now.minus(Duration.ofDays(retentionGraceDays));for(var r:rooms.findByStatusAndExpiresAtBefore("EXPIRED", purgeBefore))if(participants.count(r.roomCode)==0)rooms.delete(r);}
}
