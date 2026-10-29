package com.collaborativecanvas.server.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Service public class CanvasSnapshotService { private final CanvasStateService state; public CanvasSnapshotService(CanvasStateService state){this.state=state;} @Scheduled(fixedDelayString="${canvas.snapshot.interval-millis:30000}") public void createDueSnapshots(){state.snapshotDirtyRooms();} }
