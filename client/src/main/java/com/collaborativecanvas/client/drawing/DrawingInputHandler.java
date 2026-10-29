package com.collaborativecanvas.client.drawing;
import com.collaborativecanvas.shared.dto.*;
import com.collaborativecanvas.shared.enums.DrawingOperationType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import java.util.UUID;
import java.util.function.Consumer;
public final class DrawingInputHandler {
 private final Canvas canvas; private final Consumer<DrawingOperationMessage> send; private final String roomId; private String strokeId; private boolean eraser; private String color="#1E88E5"; private double size=6;
 public DrawingInputHandler(Canvas canvas,String roomId,Consumer<DrawingOperationMessage> send){this.canvas=canvas;this.roomId=roomId;this.send=send;canvas.setOnMousePressed(this::start);canvas.setOnMouseDragged(this::move);canvas.setOnMouseReleased(this::end);}
 public void setTool(boolean eraser,String color,double size){this.eraser=eraser;this.color=color;this.size=size;}
 private CanvasPoint point(MouseEvent e){var b=CoordinateMapper.drawableBounds(canvas.getWidth(),canvas.getHeight(),CanvasRenderer.LOGICAL_WIDTH,CanvasRenderer.LOGICAL_HEIGHT);return CoordinateMapper.normalized(e.getX(),e.getY(),b);}
 private boolean inside(MouseEvent e){var b=CoordinateMapper.drawableBounds(canvas.getWidth(),canvas.getHeight(),CanvasRenderer.LOGICAL_WIDTH,CanvasRenderer.LOGICAL_HEIGHT);return CoordinateMapper.contains(e.getX(),e.getY(),b);}
 private void start(MouseEvent e){if(!inside(e))return;strokeId=UUID.randomUUID().toString();emit(eraser?DrawingOperationType.ERASE_START:DrawingOperationType.DRAW_START,e);}
 private void move(MouseEvent e){if(strokeId!=null&&inside(e))emit(eraser?DrawingOperationType.ERASE_MOVE:DrawingOperationType.DRAW_MOVE,e);}
 private void end(MouseEvent e){if(strokeId!=null&&inside(e))emit(eraser?DrawingOperationType.ERASE_END:DrawingOperationType.DRAW_END,e);strokeId=null;}
 private void emit(DrawingOperationType type,MouseEvent e){CanvasPoint p=point(e);send.accept(new DrawingOperationMessage(UUID.randomUUID(),roomId,null,type,strokeId,p.normalizedX(),p.normalizedY(),color,size,eraser,null,java.time.Instant.now(),null));}
}
