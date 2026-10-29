package com.collaborativecanvas.client.service;
import com.collaborativecanvas.shared.dto.*;import com.collaborativecanvas.shared.enums.DrawingOperationType;
import java.util.*;import java.util.function.Consumer;
public class CanvasSyncService {private CanvasDocument document=CanvasDocument.empty();private long lastApplied;private final TreeMap<Long,DrawingOperationMessage> pending=new TreeMap<>();private final Consumer<Void> gap;
 public CanvasSyncService(Consumer<Void> gap){this.gap=gap;}
 /** Applies a complete snapshot/replay response. A malformed or gapped response never marks the canvas ready. */
 public synchronized boolean state(CanvasStateMessage state){
  if(state==null||state.snapshot()==null||state.lastSequenceNumber()<state.snapshotSequenceNumber()) { gap.accept(null); return false; }
  CanvasDocument rebuilt=state.snapshot(); long expected=state.snapshotSequenceNumber();
  var replay=new ArrayList<>(state.operationsAfterSnapshot()==null?List.<DrawingOperationMessage>of():state.operationsAfterSnapshot());
  replay.sort(Comparator.comparing(DrawingOperationMessage::sequenceNumber,Comparator.nullsLast(Long::compareTo)));
  for(var operation:replay){if(operation.sequenceNumber()==null||operation.sequenceNumber()!=expected+1){gap.accept(null);return false;}rebuilt=apply(rebuilt,operation);expected++;}
  if(expected!=state.lastSequenceNumber()){gap.accept(null);return false;}
  document=rebuilt;lastApplied=expected;pending.clear();return true;
 }
 public synchronized void receive(DrawingOperationMessage op){if(op.sequenceNumber()==null||op.sequenceNumber()<=lastApplied)return;pending.put(op.sequenceNumber(),op);while(pending.containsKey(lastApplied+1)){var next=pending.remove(lastApplied+1);document=apply(document,next);lastApplied++;}if(!pending.isEmpty()&&pending.firstKey()>lastApplied+1)gap.accept(null);}
 public synchronized CanvasDocument document(){return document;} public synchronized long lastApplied(){return lastApplied;}
 static CanvasDocument apply(CanvasDocument doc,DrawingOperationMessage op){Map<String,CanvasStroke> strokes=new LinkedHashMap<>();for(var s:doc.strokes())strokes.put(s.strokeId(),s);if(op.type()==DrawingOperationType.CLEAR_CANVAS)strokes.clear();else{var old=strokes.get(op.strokeId());var pts=old==null?new ArrayList<CanvasPoint>():new ArrayList<>(old.points());pts.add(new CanvasPoint(op.normalizedX(),op.normalizedY()));strokes.put(op.strokeId(),new CanvasStroke(op.strokeId(),op.participantId(),op.colorHex(),op.brushSize(),op.isEraser(),pts));}return new CanvasDocument(1,doc.canvasWidth(),doc.canvasHeight(),new ArrayList<>(strokes.values()));}
}
