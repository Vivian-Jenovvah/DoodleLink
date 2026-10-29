package com.collaborativecanvas.client.service;
import javafx.scene.control.Label;
public class UiNotificationService {private final Label label;public UiNotificationService(Label label){this.label=label;}public void show(String s){label.setText(s==null?"":s);}}
