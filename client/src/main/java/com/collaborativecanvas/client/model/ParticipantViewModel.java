package com.collaborativecanvas.client.model;
public record ParticipantViewModel(String displayName, boolean currentUser) { @Override public String toString(){return currentUser ? displayName+" (you)" : displayName;} }
