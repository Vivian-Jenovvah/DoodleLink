package com.collaborativecanvas.server.service;
import com.collaborativecanvas.server.entity.*;
import com.collaborativecanvas.server.exception.*;
import com.collaborativecanvas.server.model.RoomState;
import com.collaborativecanvas.server.repository.*;
import com.collaborativecanvas.shared.dto.*;
import com.collaborativecanvas.shared.enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
@Service public class CanvasStateService {
 private final RoomRepository rooms; private final CanvasSnapshotRepository snapshots; private final DrawingOperationRepository operations; private final com.fasterxml.jackson.databind.ObjectMapper mapper;
 private final ConcurrentMap<String,RoomState> states=new ConcurrentHashMap<>();
 @Value("${canvas.snapshot.operation-threshold:100}") int snapshotThreshold;
 @Value("${canvas.snapshot.keep-count:3}") int keepCount;
 @Value("${canvas.room.inactive-ttl-hours:24}") long inactiveTtlHours;
 public CanvasStateService(RoomRepository rooms,CanvasSnapshotRepository snapshots,DrawingOperationRepository operations,com.fasterxml.jackson.databind.ObjectMapper mapper){this.rooms=rooms;this.snapshots=snapshots;this.operations=operations;this.mapper=mapper;}
 private RoomState state(String roomId){return states.computeIfAbsent(roomId,k->new RoomState());}
 @Transactional public DrawingOperationMessage accept(String roomCode,String participantId,DrawingOperationMessage incoming){ RoomState s=state(roomCode);s.lock.lock();try{return acceptLocked(roomCode,participantId,incoming);}finally{s.lock.unlock();} }
 @Transactional protected DrawingOperationMessage acceptLocked(String roomCode,String participantId,DrawingOperationMessage m){
  validate(roomCode,m); RoomEntity room=rooms.findForUpdate(roomCode).orElseThrow(()->new RoomNotFoundException("This room does not exist or has expired.")); if(!"ACTIVE".equals(room.status)||room.expiresAt.isBefore(Instant.now()))throw new RoomNotFoundException("This room does not exist or has expired.");
  Optional<DrawingOperationEntity> existing=operations.findByRoomIdAndOperationId(room.id,m.operationId()); if(existing.isPresent())return toDto(existing.get());
  long sequence=room.lastSequenceNumber+1; Instant now=Instant.now(); DrawingOperationMessage committed=new DrawingOperationMessage(m.operationId(),roomCode,participantId,m.type(),m.strokeId(),m.normalizedX(),m.normalizedY(),m.colorHex(),m.brushSize(),m.isEraser(),sequence,m.clientTimestamp(),now);
  DrawingOperationEntity e=new DrawingOperationEntity();e.id=UUID.randomUUID();e.roomId=room.id;e.operationId=m.operationId();e.sequenceNumber=sequence;e.participantId=participantId;e.operationType=m.type();e.payloadJson=mapper.valueToTree(committed);e.createdAt=now;operations.save(e);room.lastSequenceNumber=sequence;room.lastActivityAt=now;room.updatedAt=now;room.expiresAt=now.plus(Duration.ofHours(inactiveTtlHours));rooms.save(room);
  if(sequence % snapshotThreshold==0) createSnapshot(room,sequence); return committed;
 }
 @Transactional(readOnly=true) public CanvasStateMessage synchronize(String roomCode,String participantId){RoomState s=state(roomCode);s.lock.lock();try{return synchronizedLocked(roomCode,participantId);}finally{s.lock.unlock();}}
 @Transactional(readOnly=true) protected CanvasStateMessage synchronizedLocked(String roomCode,String participantId){ RoomEntity room=rooms.findByRoomCode(roomCode).orElseThrow(()->new RoomNotFoundException("This room does not exist or has expired.")); if(!"ACTIVE".equals(room.status)||room.expiresAt.isBefore(Instant.now()))throw new RoomNotFoundException("This room does not exist or has expired."); long at=room.lastSequenceNumber; CanvasSnapshotEntity snap=snapshots.latestAtOrBefore(room.id,at,PageRequest.of(0,1)).stream().findFirst().orElseThrow(()->new IllegalStateException("Room snapshot is unavailable.")); CanvasDocument doc=mapper.convertValue(snap.snapshotJson,CanvasDocument.class);var replay=operations.replay(room.id,snap.snapshotSequenceNumber,at).stream().map(this::toDto).toList();return new CanvasStateMessage(roomCode,participantId,ServerEventType.CANVAS_STATE,1,snap.snapshotSequenceNumber,at,doc,replay); }
 @Transactional public void snapshotDirtyRooms(){ for(RoomEntity room:rooms.findAll()){ if(!"ACTIVE".equals(room.status))continue; RoomState s=state(room.roomCode);if(!s.lock.tryLock())continue;try{CanvasSnapshotEntity latest=snapshots.latestAtOrBefore(room.id,room.lastSequenceNumber,PageRequest.of(0,1)).stream().findFirst().orElse(null); if(latest!=null&&latest.snapshotSequenceNumber<room.lastSequenceNumber&&Duration.between(latest.createdAt,Instant.now()).getSeconds()>=30)createSnapshot(room,room.lastSequenceNumber);}finally{s.lock.unlock();} } }
 private void createSnapshot(RoomEntity room,long sequence){CanvasStateMessage state=synchronizedLocked(room.roomCode,null);CanvasDocument d=apply(state.snapshot(),state.operationsAfterSnapshot());CanvasSnapshotEntity s=new CanvasSnapshotEntity();s.id=UUID.randomUUID();s.roomId=room.id;s.snapshotSequenceNumber=sequence;s.schemaVersion=1;s.snapshotJson=mapper.valueToTree(d);s.createdAt=Instant.now();snapshots.save(s);var old=snapshots.findByRoomIdOrderBySnapshotSequenceNumberDesc(room.id);for(int i=keepCount;i<old.size();i++)snapshots.delete(old.get(i));}
 public CanvasDocument apply(CanvasDocument base,List<DrawingOperationMessage> list){Map<String,CanvasStroke> strokes=new LinkedHashMap<>();for(CanvasStroke st:base.strokes())strokes.put(st.strokeId(),st);for(DrawingOperationMessage m:list){if(m.type()==DrawingOperationType.CLEAR_CANVAS){strokes.clear();continue;}boolean erase=m.isEraser()||m.type().name().startsWith("ERASE");CanvasStroke old=strokes.get(m.strokeId());List<CanvasPoint> ps=old==null?new ArrayList<>():new ArrayList<>(old.points());ps.add(new CanvasPoint(m.normalizedX(),m.normalizedY()));strokes.put(m.strokeId(),new CanvasStroke(m.strokeId(),m.participantId(),m.colorHex(),m.brushSize(),erase,ps));}return new CanvasDocument(1,base.canvasWidth(),base.canvasHeight(),new ArrayList<>(strokes.values()));}
 private DrawingOperationMessage toDto(DrawingOperationEntity e){return mapper.convertValue(e.payloadJson,DrawingOperationMessage.class);}
 private void validate(String roomId,DrawingOperationMessage m){if(m==null||m.operationId()==null||m.type()==null)throw new InvalidDrawingOperationException("Operation ID and type are required.");if(!roomId.matches("[A-Z2-9]{6,12}"))throw new InvalidDrawingOperationException("Invalid room ID.");if(m.type()!=DrawingOperationType.CLEAR_CANVAS){if(m.strokeId()==null||m.strokeId().length()>64)throw new InvalidDrawingOperationException("A valid stroke ID is required.");if(!finiteUnit(m.normalizedX())||!finiteUnit(m.normalizedY()))throw new InvalidDrawingOperationException("Coordinates must be between 0 and 1.");if(!m.isEraser()&&(m.colorHex()==null||!m.colorHex().matches("#[0-9a-fA-F]{6}")))throw new InvalidDrawingOperationException("Color must be a six-digit hexadecimal value.");if(!Double.isFinite(m.brushSize())||m.brushSize()<1||m.brushSize()>50)throw new InvalidDrawingOperationException("Brush size must be between 1 and 50.");}}
 private boolean finiteUnit(double v){return Double.isFinite(v)&&v>=0&&v<=1;}
}
