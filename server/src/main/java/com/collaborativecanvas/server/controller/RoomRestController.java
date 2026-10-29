package com.collaborativecanvas.server.controller;
import com.collaborativecanvas.server.service.*;
import com.collaborativecanvas.shared.dto.*;
import com.collaborativecanvas.shared.protocol.ApiPaths;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ApiPaths.ROOMS)
public class RoomRestController {
 private final RoomService rooms; private final ParticipantService participants;
 public RoomRestController(RoomService rooms,ParticipantService participants){this.rooms=rooms;this.participants=participants;}
 @PostMapping public ResponseEntity<CreateRoomResponse> create(@Valid @RequestBody CreateRoomRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(rooms.create(r.displayName()));}
 @GetMapping("/{roomId}") public RoomDetailsResponse details(@PathVariable String roomId){return rooms.details(roomId,participants.count(roomId));}
}
