package com.collaborativecanvas.client.websocket;
public interface StompClient { void connect(String serverUrl, StompEventListener listener); void subscribeRoom(String roomId); void send(String destination, Object payload); void disconnect(); boolean connected(); }
