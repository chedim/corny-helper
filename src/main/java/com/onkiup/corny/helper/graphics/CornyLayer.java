package com.onkiup.corny.helper.graphics;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Renders a triple-size corny bits
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : CornyLayout
 * @created : Sunday Mar 29, 2020 15:15:50 EDT
 */

public class CornyLayer extends JPanel implements LayoutManager {
  private String name;
  private Collection<CornyButton> buttons = new ArrayList<>();
  // Corny triplet layout: 6 columns by 3 rows
  private CornyButton[][] matrix = new CornyButton[6][3];

  public CornyLayer(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }


  private void rebuildMatrix() {

  }

  @Override
  public void addLayoutComponent(String name, Component comp) {
    if (comp instanceof CornyButton) {
      throw new IllegalArgumentException("Unable to add the component: not a CornyButton");
    }
    buttons.add((CornyButton)comp);
  }

  @Override
  public void removeLayoutComponent(Component comp) {
    buttons.remove(comp);
  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return null;
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return null;
  }

  @Override
  public void layoutContainer(Container parent) {

  }
}
