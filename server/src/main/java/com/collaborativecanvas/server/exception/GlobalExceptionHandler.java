package com.collaborativecanvas.server.exception;
import com.collaborativecanvas.shared.dto.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;
@RestControllerAdvice public class GlobalExceptionHandler {
 private ResponseEntity<ErrorMessage> error(HttpStatus s, String c, String m) { return ResponseEntity.status(s).body(new ErrorMessage(c,m,Instant.now(),UUID.randomUUID().toString())); }
 @ExceptionHandler(RoomNotFoundException.class) ResponseEntity<ErrorMessage> room(RoomNotFoundException e) { return error(HttpStatus.NOT_FOUND,"ROOM_NOT_FOUND",e.getMessage()); }
 @ExceptionHandler({InvalidDrawingOperationException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class}) ResponseEntity<ErrorMessage> invalid(Exception e) { return error(HttpStatus.BAD_REQUEST,"INVALID_REQUEST",e.getMessage()); }
 @ExceptionHandler(Exception.class) ResponseEntity<ErrorMessage> other(Exception e) { return error(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR","The server could not complete this request."); }
}
