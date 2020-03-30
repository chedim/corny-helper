package com.onkiup.corny.helper.arguments;

import com.onkiup.linker.grammars.commons.strings.ACommonString;

import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : CornyConnector
 * @created : Monday Mar 30, 2020 02:41:13 EDT
 */

public class CornyConnector implements AnArgument, HidServicesListener {
  private static final String MARKER = "--connector=";
  private ACommonString connectorName;

  private static transient final int VENDOR_ID = 0xFEED;
  private static transient final int DEVICE_ID = 0x0000;

  private HidServices hidServices;

  public CornyConnector() {
    HidServicesSpecification spec = new HidServicesSpecification();
    spec.setAutoShutdown(true);
    spec.setScanInterval(500);
    spec.setPauseInterval(5000);
    spec.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);

    hidServices = HidManager.getHidServices(spec);
    hidServices.addHidServicesListener(this);
  }

  @Override
  public void hidDeviceAttached(HidServicesEvent event) {
    event.getHidDevice();
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
