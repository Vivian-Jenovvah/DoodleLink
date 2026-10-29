package com.collaborativecanvas.server.service;
import com.collaborativecanvas.server.entity.*;
import com.collaborativecanvas.server.exception.RoomNotFoundException;
import com.collaborativecanvas.server.repository.*;
import com.collaborativecanvas.shared.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.*;
import java.util.*;
@Service public class RoomService {
 private static final String ALPHABET="ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; private final SecureRandom random=new SecureRandom();
 private final RoomRepository rooms; private final CanvasSnapshotRepository snapshots; private final com.fasterxml.jackson.databind.ObjectMapper mapper;
 @Value("${canvas.room.inactive-ttl-hours:24}") long ttlHours;
 public RoomService(RoomRepository rooms, CanvasSnapshotRepository snapshots, com.fasterxml.jackson.databind.ObjectMapper mapper) { this.rooms=rooms; this.snapshots=snapshots; this.mapper=mapper; }
 @Transactional public CreateRoomResponse create(String displayName) {
  String clean=cleanName(displayName); Instant now=Instant.now(); RoomEntity room=new RoomEntity(); room.id=UUID.randomUUID(); room.roomCode=uniqueCode(); room.status="ACTIVE"; room.createdAt=now; room.updatedAt=now; room.lastActivityAt=now; room.expiresAt=now.plus(Duration.ofHours(ttlHours)); rooms.save(room);
  CanvasSnapshotEntity snapshot=new CanvasSnapshotEntity(); snapshot.id=UUID.randomUUID(); snapshot.roomId=room.id; snapshot.snapshotSequenceNumber=0; snapshot.schemaVersion=1; snapshot.snapshotJson=mapper.valueToTree(CanvasDocument.empty()); snapshot.createdAt=now; snapshots.save(snapshot);
  return new CreateRoomResponse(room.roomCode, UUID.randomUUID().toString(), clean, room.status, 0, now);
 }
 @Transactional(readOnly=true) public RoomDetailsResponse details(String code, int participantCount) { RoomEntity r=active(code); return new RoomDetailsResponse(r.roomCode,r.status,participantCount,r.lastSequenceNumber,r.createdAt,r.lastActivityAt); }
 @Transactional(readOnly=true) public RoomEntity active(String code) { RoomEntity r=rooms.findByRoomCode(code.trim().toUpperCase(Locale.ROOT)).orElseThrow(()->new RoomNotFoundException("This room does not exist or has expired.")); if (!"ACTIVE".equals(r.status)||r.expiresAt.isBefore(Instant.now())) throw new RoomNotFoundException("This room does not exist or has expired."); return r; }
 private String uniqueCode(){ for(int n=0;n<20;n++){StringBuilder b=new StringBuilder(8);for(int i=0;i<8;i++)b.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));String c=b.toString();if(!rooms.existsByRoomCode(c))return c;} throw new IllegalStateException("Could not allocate a room code."); }
 public String cleanName(String name){ if(name==null) throw new IllegalArgumentException("Display name is required."); String v=name.trim(); if(v.isEmpty()||v.length()>32)throw new IllegalArgumentException("Display name must be 1 to 32 characters."); return v; }
}
