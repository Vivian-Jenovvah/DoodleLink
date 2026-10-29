package com.collaborativecanvas.client.util;
import javafx.application.Platform;
public final class FxThread { private FxThread(){} public static void run(Runnable r){if(Platform.isFxApplicationThread())r.run();else Platform.runLater(r);} }
