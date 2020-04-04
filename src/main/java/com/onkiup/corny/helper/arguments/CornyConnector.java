package com.onkiup.corny.helper.arguments;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.onkiup.corny.helper.rawhid.ARawHidCommand;
import com.onkiup.corny.helper.rawhid.GetLayerState;
import com.onkiup.corny.helper.rawhid.GetProtocolVersion;
import com.onkiup.linker.grammars.commons.strings.ACommonString;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : CornyConnector
 * @created : Monday Mar 30, 2020 02:41:13 EDT
 */

public class CornyConnector implements AnArgument, HidServicesListener, NativeKeyListener {
  private static final String MARKER = "--connector=";
  private ACommonString connectorName;

  private static transient final Logger log = LoggerFactory.getLogger(CornyConnector.class);
  private static transient final int VENDOR_ID = 0xFFFFFEED;
  private static transient final int DEVICE_ID = 0x0000;
  private static final int MSGSIZE = 31;
  private static final int INTERFACE = 1;

  private transient HidServices hidServices;
  private transient Set<HidDevice> boards = new HashSet<>();
  private transient HidDevice mainBoard;
  private transient int layerState = 0;
  private transient static final Set<Integer> keysHeld = Collections.synchronizedSet(new HashSet<>());
  private transient Set<Listener> listeners = new HashSet<>();
  private transient Timer timer = new Timer();

  public CornyConnector() {
    HidServicesSpecification spec = new HidServicesSpecification();
    spec.setAutoShutdown(true);
    spec.setScanInterval(500);
    spec.setPauseInterval(5000);
    spec.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);

    hidServices = HidManager.getHidServices(spec);
    hidServices.addHidServicesListener(this);

    for (HidDevice device : hidServices.getAttachedHidDevices()) {
      if (isCorny(device)) {
        registerBoard(device);
      }
    }
    hidServices.start();
    timer.scheduleAtFixedRate(new LayerStatePoller(), 10, 10);
  }

  @Override
  public void hidDeviceAttached(HidServicesEvent event) {
    if (isCorny(event)) {
      registerBoard(event.getHidDevice());
    } else {
      log.debug("not corny: {}", event.getHidDevice());
    }
  }

  public Set<HidDevice> boards() {
    return boards;
  }

  public void setMainBoard(HidDevice board) {
    mainBoard = board;
  }

  private synchronized void registerBoard(HidDevice corny) {
    if (!boards.contains(corny)) {
      log.debug("attached: {}", corny.getId());
      corny.open();
      boards.add(corny);
      // execute(InitializeInterface.class, corny);
      byte protover = execute(GetProtocolVersion.class, corny);
      log.debug("Protocol version: {}", protover);
      int layers = execute(GetLayerState.class, corny);
      log.debug("Layer state: {}", layers);

      String enabledLayers = "";
      for (int i = 0; i < 32; i++) {
        if (0 != (layers & (1 << i))) {
          enabledLayers += " [" + i + "]";
        }
      }
      log.debug("Enabled layers: {}", enabledLayers);

      if (mainBoard == null) {
        setMainBoard(corny);
      }
    }
  }

  public Integer layerState(HidDevice source) {
    return execute(GetLayerState.class, source);
  }

  @Override
  public void hidDeviceDetached(HidServicesEvent event) {
    if (isCorny(event)) {
      HidDevice corny = event.getHidDevice();
      log.debug("detached: {}", corny);
      boards.remove(corny);
    }
  }

  public <R, T extends ARawHidCommand<R>> R execute(Class<T> command, HidDevice device) {
    try {
      if (!device.isOpen()) {
        device.open();
      }

      log.debug("Sendig {} to {}", command.getSimpleName(), device);
      T cmd = command.newInstance();
      byte code = cmd.code();
      byte[] data = new byte[MSGSIZE];
      cmd.data(data);

      log.debug("Data: {}\nSize: {}\nCode: {}", data, MSGSIZE, code);
      device.write(data, MSGSIZE, code);
      log.debug("Sent, reading response...");
      byte[] response = new byte[MSGSIZE];
      device.read(response, 100);
      log.debug("Response: {}", response);
      return cmd.response(response);

    } catch (Exception e) {
      log.error("FAIL", e);
      throw new RuntimeException("Failed to send command " + command.getCanonicalName() + " to " + device, e);
    }
  }

  public static boolean isCorny(HidServicesEvent event) {
    return isCorny(event.getHidDevice());
  }

  public static boolean isCorny(HidDevice device) {
    return device.isVidPidSerial(VENDOR_ID, DEVICE_ID, null) && device.getInterfaceNumber() == INTERFACE;
  }

  @Override
  public void hidFailure(HidServicesEvent event) {
    log.error("failed");
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

  }

  public void addListener(Listener listener) {
    log.info("Registering new listener: {}", listener);
    listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    boolean old = keysHeld.contains(e.getKeyCode());
    keysHeld.add(e.getKeyCode());
    if (!old) {
      listeners.forEach(listener -> listener.keyHeld(e.getKeyCode(), true));
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent e) {
    keysHeld.remove(e.getKeyCode());
    listeners.forEach(listener -> listener.keyHeld(e.getKeyCode(), false));
  }

  public static interface Listener {
    void keyHeld(int code, boolean held);

    void layerShift(int layer, boolean active);
  }

  public class LayerStatePoller extends TimerTask {

    @Override
    public void run() {
      if (mainBoard != null) {
        updateLayerState();
      }
    }
  }

  public void updateLayerState() {
    updateLayerState(execute(GetLayerState.class, mainBoard));
  }

  public void updateLayerState(int newState) {
    int oldState = layerState;
    layerState = newState;
    if (newState != oldState) {
      log.debug("layer state: {}", layerState);
      int changed = oldState ^ newState;
      for (int i = 0; i < 32; i++) {
        int old = oldState & 1, next = newState & 1;
        if (old != next) {
          for (Listener listener : listeners) {
            listener.layerShift(i, next > 0);
          }
        }
        oldState = oldState >> 1;
        newState = newState >> 1;
      }
    }
  }
}
