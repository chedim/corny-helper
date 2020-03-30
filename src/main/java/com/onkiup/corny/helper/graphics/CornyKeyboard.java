package com.onkiup.corny.helper.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Window;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.onkiup.corny.helper.arguments.commands.Render;

import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : CornyKeyboard
 * @created : Sunday Mar 29, 2020 15:50:41 EDT
 */

public class CornyKeyboard extends JFrame implements LayoutManager, NativeKeyListener, HidServicesListener {
  public static final Color op = new Color(0, 0, 0, 0);
  public static final Color bg = new Color(0, 0, 1, 0.7f);
  public static final Color fg = new Color(1, 1, 1, 0.3f);
  private static final Set<Integer> keysHeld = Collections.synchronizedSet(new HashSet<>());

  private Collection<CornyLayer> layers;
  private Map<String, CornyLayer> index = new HashMap<>();
  private CornyLayer currentLayer;
  private Container parent;
  private int pressed;

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

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    testPaint(g);
  }

  public void testPaint(Graphics g) {
    try {
      System.out.println("repaint");
      g.setColor(op);
      g.fillRect(0, 0, 500, 500);
      g.setColor(pick(bg, fg));
      g.fillRect(200, 200, 100, 100);
      g.setColor(pick(fg, bg));
      char[] text = new char[] { 'A' };
      g.drawChars(text, 0, 1, 250, 250);
    } catch (Exception e) {
      LoggerFactory.getLogger(Render.class).error("Failed to fix i3", e);
    }
  }

  private Color pick(Color normal, Color pressed) {
    System.out.println("Pressed == " + keysHeld.size());
    return (keysHeld.size() == 0 ? normal : pressed);
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

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    boolean old = keysHeld.contains(e.getKeyCode());
    keysHeld.add(e.getKeyCode());
    if (!old) {
      new Thread(this::repaint).start();
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent e) {
    keysHeld.remove(e.getKeyCode());
    new Thread(() -> {
      try {
        Thread.sleep(200);
        repaint();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }).start();
  }

  @Override
  public void hidDeviceAttached(HidServicesEvent event) {

  }

  @Override
  public void hidDeviceDetached(HidServicesEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void hidFailure(HidServicesEvent event) {
    // TODO Auto-generated method stub

  }

}
