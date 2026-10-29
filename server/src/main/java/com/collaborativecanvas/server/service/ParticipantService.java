package com.collaborativecanvas.server.service;
import com.collaborativecanvas.server.model.ParticipantSession;
import com.collaborativecanvas.shared.dto.ParticipantDto;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Service public class ParticipantService {
 private final Map<String,ParticipantSession> sessions=new ConcurrentHashMap<>();
 public ParticipantSession join(String sessionId,String roomId,String displayName){ ParticipantSession p=new ParticipantSession(sessionId,roomId,UUID.randomUUID().toString(),displayName); sessions.put(sessionId,p); return p; }
 public Optional<ParticipantSession> get(String sessionId){return Optional.ofNullable(sessions.get(sessionId));}
 public Optional<ParticipantSession> leave(String sessionId){return Optional.ofNullable(sessions.remove(sessionId));}
 public boolean isMember(String sessionId,String roomId){return get(sessionId).map(p->p.roomId().equals(roomId)).orElse(false);}
 public List<ParticipantDto> list(String roomId){var all=sessions.values().stream().filter(p->p.roomId().equals(roomId)).sorted(Comparator.comparing(ParticipantSession::displayName)).toList();return all.stream().map(p->new ParticipantDto(roomId,p.participantId(),p.displayName(),false,all.size(),Instant.now())).toList();}
 public int count(String roomId){return (int)sessions.values().stream().filter(p->p.roomId().equals(roomId)).count();}
}
