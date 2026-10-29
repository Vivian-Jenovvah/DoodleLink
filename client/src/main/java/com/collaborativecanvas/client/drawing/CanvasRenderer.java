package com.collaborativecanvas.client.drawing;
import com.collaborativecanvas.shared.dto.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
public class CanvasRenderer {
 public static final double LOGICAL_WIDTH=1200, LOGICAL_HEIGHT=800;
 public void render(Canvas canvas,com.collaborativecanvas.shared.dto.CanvasDocument document){GraphicsContext g=canvas.getGraphicsContext2D();g.setGlobalBlendMode(BlendMode.SRC_OVER);g.setFill(Color.WHITE);g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());var b=CoordinateMapper.drawableBounds(canvas.getWidth(),canvas.getHeight(),LOGICAL_WIDTH,LOGICAL_HEIGHT);g.setStroke(Color.web("#d9e0ea"));g.strokeRect(b.x(),b.y(),b.width(),b.height());for(CanvasStroke stroke:document.strokes())drawStroke(g,stroke,b);}
 private void drawStroke(GraphicsContext g,CanvasStroke s,CoordinateMapper.Bounds b){if(s.points().isEmpty())return;g.setLineWidth(Math.max(1,s.brushSize()*b.width()/LOGICAL_WIDTH));g.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);g.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);g.setGlobalBlendMode(BlendMode.SRC_OVER);g.setStroke(s.isEraser()?Color.WHITE:Color.web(s.colorHex()));CanvasPoint first=s.points().getFirst();if(s.points().size()==1){double x=CoordinateMapper.x(first,b),y=CoordinateMapper.y(first,b);g.strokeLine(x,y,x+.01,y+.01);}else{g.beginPath();g.moveTo(CoordinateMapper.x(first,b),CoordinateMapper.y(first,b));for(int i=1;i<s.points().size();i++){CanvasPoint p=s.points().get(i);g.lineTo(CoordinateMapper.x(p,b),CoordinateMapper.y(p,b));}g.stroke();}g.setGlobalBlendMode(BlendMode.SRC_OVER);}
}
