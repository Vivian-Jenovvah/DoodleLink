package com.collaborativecanvas.client.websocket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
public final class ReconnectManager implements AutoCloseable {private final ScheduledExecutorService scheduler=Executors.newSingleThreadScheduledExecutor(r->{Thread t=new Thread(r,"canvas-reconnect");t.setDaemon(true);return t;});private final AtomicBoolean scheduled=new AtomicBoolean();public void retry(Runnable attempt){if(scheduled.compareAndSet(false,true))scheduler.schedule(()->{scheduled.set(false);attempt.run();},2,TimeUnit.SECONDS);}public void connected(){scheduled.set(false);}@Override public void close(){scheduler.shutdownNow();}}
