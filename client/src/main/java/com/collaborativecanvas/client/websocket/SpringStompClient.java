package com.collaborativecanvas.client.websocket;
import com.collaborativecanvas.shared.protocol.WebSocketDestinations;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;import java.util.concurrent.atomic.AtomicReference;
public class SpringStompClient implements StompClient {
 private final AtomicReference<StompSession> session=new AtomicReference<>(); private volatile WebSocketStompClient client; private volatile StompEventListener listener;
 @Override public void connect(String base,StompEventListener listener){this.listener=listener;try{client=new WebSocketStompClient(new StandardWebSocketClient());MappingJackson2MessageConverter converter=new MappingJackson2MessageConverter();converter.setObjectMapper(new ObjectMapper().findAndRegisterModules());client.setMessageConverter(converter);String endpoint=base.trim().replaceFirst("^http","ws").replaceAll("/+$","")+WebSocketDestinations.ENDPOINT;client.connectAsync(endpoint,new StompSessionHandlerAdapter(){@Override public void afterConnected(StompSession s,StompHeaders h){session.set(s);s.subscribe(WebSocketDestinations.USER_MESSAGES,frames());listener.onConnected();}@Override public void handleTransportError(StompSession s,Throwable e){session.compareAndSet(s,null);listener.onDisconnected(e);}@Override public void handleException(StompSession s,StompCommand c,StompHeaders h,byte[] p,Throwable e){session.compareAndSet(s,null);listener.onDisconnected(e);}});}catch(Exception e){listener.onDisconnected(e);}}
 @Override public void subscribeRoom(String roomId){StompSession s=session.get();if(s==null)throw new IllegalStateException("Not connected");s.subscribe(WebSocketDestinations.canvasTopic(roomId),frames());s.subscribe(WebSocketDestinations.participantsTopic(roomId),frames());}
 private StompFrameHandler frames(){return new StompFrameHandler(){public Type getPayloadType(StompHeaders x){return byte[].class;}public void handleFrame(StompHeaders x,Object p){StompEventListener l=listener;if(l!=null)l.onMessage(new String((byte[])p,java.nio.charset.StandardCharsets.UTF_8));}};}
 @Override public void send(String destination,Object payload){StompSession s=session.get();if(s==null||!s.isConnected())throw new IllegalStateException("Not connected to the collaboration server.");s.send(destination,payload);}
 @Override public void disconnect(){StompSession s=session.getAndSet(null);if(s!=null&&s.isConnected())s.disconnect();if(client!=null)client.stop();}
 @Override public boolean connected(){StompSession s=session.get();return s!=null&&s.isConnected();}
}
