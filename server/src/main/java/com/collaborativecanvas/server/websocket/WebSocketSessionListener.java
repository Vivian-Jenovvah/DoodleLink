package com.collaborativecanvas.server.websocket;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
@Component public class WebSocketSessionListener {private final CanvasMessageController controller;public WebSocketSessionListener(CanvasMessageController controller){this.controller=controller;}@EventListener public void disconnected(SessionDisconnectEvent e){controller.disconnected(e.getSessionId());}}
