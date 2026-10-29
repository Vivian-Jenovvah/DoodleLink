package com.collaborativecanvas.server.service;
import com.collaborativecanvas.shared.dto.DrawingOperationMessage;
import org.springframework.stereotype.Service;
@Service public class DrawingOperationService { private final CanvasStateService state; public DrawingOperationService(CanvasStateService state){this.state=state;} public DrawingOperationMessage accept(String room,String participant,DrawingOperationMessage op){return state.accept(room,participant,op);} }
