package com.onkiup.corny.helper.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Window;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : CornyKeyboard
 * @created : Sunday Mar 29, 2020 15:50:41 EDT
 */

public class CornyKeyboard extends JFrame implements LayoutManager {
  public static final Color op = new Color(0, 0, 0, 0);
  public static final Color bg = new Color(0, 0, 1, 0.7f);
  public static final Color fg = new Color(1, 1, 1, 0.3f);

  private Collection<CornyLayer> layers;
  private Map<String, CornyLayer> index = new HashMap<>();
  private CornyLayer currentLayer;
  private Container parent;

  public CornyKeyboard() {
    super("Corny helper");
    setAlwaysOnTop(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setUndecorated(true);
    setType(Window.Type.POPUP);
    setBackground(new Color(0, 0, 0, 0));
    setFocusableWindowState(false);
    setFocusable(false);
    setSize(500, 500);
  }

  public Optional<CornyButton> findKey(int rawCode) {
    return layers.stream()
      .map(layer -> layer.findKey(rawCode))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .findFirst();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    // TODO: layout the components and render them 
  }

  @Override
  public void addLayoutComponent(String name, Component comp) {
    if (comp instanceof CornyLayer) {
      addLayer((CornyLayer) comp);
    } else {
      throw new IllegalArgumentException("Unable to add the component: not a CornyLayer");
    }
  }

  public CornyKeyboard addLayer(CornyLayer layer) {
    if (!layers.contains(layer)) {
      if (index.containsKey(layer.name())) {
        throw new IllegalArgumentException("Duplicate layer name '" + layer.name() + "'");
      }
      layers.add(layer);
      index.put(layer.name(), layer);
      layer.layoutContainer(this);
    }
    return this;
  }

  public CornyLayer currentLayer() {
    return currentLayer;
  }

  @Override
  public void removeLayoutComponent(Component comp) {
    if (layers.contains(comp)) {
      layers.remove(comp);
      index.remove(((CornyLayer) comp).name());
    }
  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return currentLayer().preferredLayoutSize(parent);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return preferredLayoutSize(parent);
  }

  @Override
  public void layoutContainer(Container parent) {
    this.parent = parent;
  }

}
