package com.collaborativecanvas.client.config;
import java.util.Properties;
public final class ClientConfigLoader { private ClientConfigLoader(){} public static ClientConfig load(){Properties p=new Properties();try(var in=ClientConfigLoader.class.getResourceAsStream("/application.properties")){if(in!=null)p.load(in);}catch(Exception ignored){}return new ClientConfig(System.getProperty("canvas.serverUrl",p.getProperty("canvas.server-url","http://localhost:8080")));} }
