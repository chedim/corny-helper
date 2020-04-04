package com.onkiup.corny.helper.rawhid;

import java.util.function.Consumer;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : GetProtocolVersion
 * @created : Monday Mar 30, 2020 23:22:44 EDT
 */
public class GetProtocolVersion implements ARawHidCommand<Byte> {

  @Override
  public byte code() {
    return 1;
  }

  @Override
  public void data(byte[] buffer) {
  }

  @Override
  public Byte response(byte[] data) {
    return data[1];
  }
}
