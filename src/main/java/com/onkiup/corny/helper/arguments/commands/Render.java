package com.onkiup.corny.helper.arguments.commands;

import com.onkiup.corny.helper.arguments.ACommand;
import com.onkiup.corny.helper.arguments.ArgumentList;
import com.onkiup.corny.helper.graphics.CornyKeyboard;

import org.jnativehook.GlobalScreen;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Render
 * @created : Sunday Mar 29, 2020 00:31:55 EDT
 */

public class Render implements ACommand {

  private static final String CMD = "render";

  private transient CornyKeyboard frame;

  @Override
  public int execute(ArgumentList arguments) {
    frame = new CornyKeyboard();
    try {
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(frame);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    frame.setVisible(true);
    frame.setFocusableWindowState(false);
    return -1;
  }

}
