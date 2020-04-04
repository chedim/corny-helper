package com.onkiup.corny.helper.arguments.commands;

import com.onkiup.corny.helper.arguments.ACommand;
import com.onkiup.corny.helper.arguments.ArgumentList;
import com.onkiup.corny.helper.arguments.CornyConnector;
import com.onkiup.corny.helper.graphics.CornyKeyboard;

import org.jnativehook.GlobalScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Render
 * @created : Sunday Mar 29, 2020 00:31:55 EDT
 */

public class Render implements ACommand {

  private static final String CMD = "render";

  private transient CornyKeyboard frame;
  private transient CornyConnector connector;

  private static transient final Logger log = LoggerFactory.getLogger(Render.class);

  @Override
  public int execute(ArgumentList arguments) {
    frame = new CornyKeyboard();
    connector = arguments.getLast(CornyConnector.class).orElseGet(CornyConnector::new);
    connector.addListener(new KeyListener());

    try {
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(connector);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    frame.setVisible(true);
    frame.setFocusableWindowState(false);
    return -1;
  }

  private class KeyListener implements CornyConnector.Listener {

    @Override
    public void keyHeld(int code, boolean held) {
    }

    @Override
    public void layerShift(int layer, boolean active) {
      log.warn("Layer {}: {}", active ? "ON" : "OFF", layer);
    }
  }
}
