package com.collaborativecanvas.server.model;
import java.util.concurrent.locks.ReentrantLock;
public final class RoomState { public final ReentrantLock lock = new ReentrantLock(true); }
