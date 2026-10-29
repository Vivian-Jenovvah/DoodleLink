package com.collaborativecanvas.server.websocket;
import com.collaborativecanvas.server.exception.*;
import com.collaborativecanvas.server.service.*;
import com.collaborativecanvas.shared.dto.*;
import com.collaborativecanvas.shared.protocol.WebSocketDestinations;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.time.Instant;
@Controller public class CanvasMessageController {
 private final RoomService rooms; private final ParticipantService participants; private final CanvasStateService state; private final DrawingOperationService operations; private final SimpMessagingTemplate messaging;
 public CanvasMessageController(RoomService rooms,ParticipantService participants,CanvasStateService state,DrawingOperationService operations,SimpMessagingTemplate messaging){this.rooms=rooms;this.participants=participants;this.state=state;this.operations=operations;this.messaging=messaging;}
 @MessageMapping("/rooms/{roomId}/join") @SendToUser(value="/queue/messages",broadcast=false)
 public CanvasStateMessage join(@DestinationVariable String roomId,@Payload JoinRoomRequest request,SimpMessageHeaderAccessor headers){if(request==null)throw new InvalidDrawingOperationException("A display name is required.");String sid=session(headers);String clean=rooms.cleanName(request.displayName());rooms.active(roomId);participants.leave(sid);var participant=participants.join(sid,roomId,clean);broadcastParticipants(roomId);return state.synchronize(roomId,participant.participantId());}
 @MessageMapping("/rooms/{roomId}/sync") @SendToUser(value="/queue/messages",broadcast=false)
 public CanvasStateMessage sync(@DestinationVariable String roomId,SimpMessageHeaderAccessor headers){var participant=requireMember(session(headers),roomId);return state.synchronize(roomId,participant.participantId());}
 @MessageMapping("/rooms/{roomId}/operations")
 public void operation(@DestinationVariable String roomId,@Payload DrawingOperationMessage request,SimpMessageHeaderAccessor headers){var participant=requireMember(session(headers),roomId);var committed=operations.accept(roomId,participant.participantId(),request);messaging.convertAndSend(WebSocketDestinations.canvasTopic(roomId),committed);}
 @MessageMapping("/rooms/{roomId}/leave") public void leave(@DestinationVariable String roomId,SimpMessageHeaderAccessor headers){var p=participants.leave(session(headers));if(p.isPresent())broadcastParticipants(roomId);}
 public void disconnected(String sessionId){participants.leave(sessionId).ifPresent(p->broadcastParticipants(p.roomId()));}
 private void broadcastParticipants(String room){messaging.convertAndSend(WebSocketDestinations.participantsTopic(room),participants.list(room));}
 private com.collaborativecanvas.server.model.ParticipantSession requireMember(String sid,String room){return participants.get(sid).filter(p->p.roomId().equals(room)).orElseThrow(()->new InvalidDrawingOperationException("Your connection is not a member of this room."));}
 private String session(SimpMessageHeaderAccessor h){if(h.getSessionId()==null)throw new InvalidDrawingOperationException("WebSocket session is unavailable.");return h.getSessionId();}
 @MessageExceptionHandler @SendToUser(value="/queue/messages",broadcast=false) public ErrorMessage error(Exception e){String code=e instanceof RoomNotFoundException?"ROOM_NOT_FOUND":e instanceof InvalidDrawingOperationException?"INVALID_OPERATION":"INTERNAL_ERROR";String message=e instanceof RoomNotFoundException||e instanceof InvalidDrawingOperationException?e.getMessage():"The server could not process that message.";return new ErrorMessage(code,message,Instant.now(),java.util.UUID.randomUUID().toString());}
}
