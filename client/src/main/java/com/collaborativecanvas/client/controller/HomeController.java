package com.collaborativecanvas.client.controller;
import com.collaborativecanvas.client.CollaborativeCanvasClient;
import com.collaborativecanvas.client.service.RoomApiClient;
import com.collaborativecanvas.client.util.FxThread;
import javafx.fxml.FXML;import javafx.scene.control.*;
public class HomeController {
 @FXML private TextField displayName; @FXML private TextField serverUrl; @FXML private TextField roomId; @FXML private Label statusMessage; @FXML private Button createButton; @FXML private Button joinButton;
 private CollaborativeCanvasClient app; private final RoomApiClient api=new RoomApiClient();
 public void setApp(CollaborativeCanvasClient app){this.app=app;serverUrl.setText(app.config().serverUrl());}
 @FXML private void createRoom(){String name=validName();if(name==null)return;busy(true,"Creating room…");try{String base=RoomApiClient.normalize(serverUrl.getText());api.create(base,name).whenComplete((r,e)->FxThread.run(()->{if(e!=null)busy(false,message(e));else app.openWorkspace(name,base,r.roomId(),r.participantId());}));}catch(Exception e){busy(false,e.getMessage());}}
 @FXML private void joinRoom(){String name=validName();if(name==null)return;String room=roomId.getText()==null?"":roomId.getText().trim().toUpperCase();if(!room.matches("[A-Z2-9]{6,12}")){busy(false,"Enter a valid room ID.");return;}busy(true,"Checking room…");try{String base=RoomApiClient.normalize(serverUrl.getText());api.details(base,room).whenComplete((r,e)->FxThread.run(()->{if(e!=null)busy(false,message(e));else app.openWorkspace(name,base,room,null);}));}catch(Exception e){busy(false,e.getMessage());}}
 private String validName(){String name=displayName.getText()==null?"":displayName.getText().trim();if(name.isEmpty()||name.length()>32){busy(false,"Enter a display name between 1 and 32 characters.");return null;}return name;}
 private void busy(boolean b,String text){createButton.setDisable(b);joinButton.setDisable(b);statusMessage.setText(text);}
 private String message(Throwable e){Throwable x=e;while(x.getCause()!=null)x=x.getCause();return x.getMessage()==null?"Could not reach the server. Check the URL and try again.":x.getMessage();}
}
