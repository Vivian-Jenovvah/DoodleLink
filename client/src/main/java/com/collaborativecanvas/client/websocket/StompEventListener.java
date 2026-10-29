package com.collaborativecanvas.client.websocket;
public interface StompEventListener { void onConnected(); void onMessage(String json); void onDisconnected(Throwable cause); }
