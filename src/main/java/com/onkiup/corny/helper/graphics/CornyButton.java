package com.onkiup.corny.helper.graphics;

import javax.swing.JPanel;

import java.awt.*;
/**
 * @author      : chedim (chedim@chedim-Surface-Pro-3)
 * @file        : CornyButton
 * @created     : Sunday Mar 29, 2020 15:35:12 EDT
 */

public class CornyButton extends JPanel {
  private String label;
  private int keyCode;
  private boolean isPressed;

  public boolean isKey(int code) {
    return keyCode == code;
  }
  
  @Override
  public void paint(Graphics g) {

  }
}


