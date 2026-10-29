package com.collaborativecanvas.shared.protocol;

public final class WebSocketDestinations {
    public static final String ENDPOINT = "/ws";
    public static final String APPLICATION_PREFIX = "/app";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String USER_MESSAGES = "/user/queue/messages";
    public static String join(String roomId) { return "/app/rooms/" + roomId + "/join"; }
    public static String leave(String roomId) { return "/app/rooms/" + roomId + "/leave"; }
    public static String operations(String roomId) { return "/app/rooms/" + roomId + "/operations"; }
    public static String sync(String roomId) { return "/app/rooms/" + roomId + "/sync"; }
    public static String canvasTopic(String roomId) { return "/topic/rooms/" + roomId + "/canvas"; }
    public static String participantsTopic(String roomId) { return "/topic/rooms/" + roomId + "/participants"; }
    private WebSocketDestinations() { }
}
