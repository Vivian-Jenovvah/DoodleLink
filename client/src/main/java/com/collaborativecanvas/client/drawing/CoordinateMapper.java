package com.collaborativecanvas.client.drawing;
import com.collaborativecanvas.shared.dto.CanvasPoint;
public final class CoordinateMapper {
 public record Bounds(double x,double y,double width,double height) { }
 private CoordinateMapper() { }
 public static Bounds drawableBounds(double width,double height,double logicalWidth,double logicalHeight){double scale=Math.min(width/logicalWidth,height/logicalHeight);double w=logicalWidth*scale,h=logicalHeight*scale;return new Bounds((width-w)/2,(height-h)/2,w,h);}
 public static CanvasPoint normalized(double x,double y,Bounds b){return new CanvasPoint((x-b.x())/b.width(),(y-b.y())/b.height());}
 public static double x(CanvasPoint p,Bounds b){return b.x()+p.normalizedX()*b.width();} public static double y(CanvasPoint p,Bounds b){return b.y()+p.normalizedY()*b.height();}
 public static boolean contains(double x,double y,Bounds b){return x>=b.x()&&x<=b.x()+b.width()&&y>=b.y()&&y<=b.y()+b.height();}
}
