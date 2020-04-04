package com.onkiup.corny.helper.rawhid;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : GetProtocolVersion
 * @created : Monday Mar 30, 2020 23:22:44 EDT
 */
public class GetLayerState implements ARawHidCommand<Integer> {
  private static final Logger log = LoggerFactory.getLogger(GetLayerState.class);
  @Override
  public byte code() {
    return 2;
  }

  @Override
  public void data(byte[] buffer) {
  }

  @Override
  public Integer response(byte[] data) {
//    log.info("raw response: {}", data);
    return ((data[1] & 0xFF) << 24) |  
      ((data[2] & 0xFF) << 16) |
      ((data[3] & 0xFF) << 8) |
      ((data[4] & 0xFF) << 0);
  }
}
