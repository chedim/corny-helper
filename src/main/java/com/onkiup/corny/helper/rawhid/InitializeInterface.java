package com.onkiup.corny.helper.rawhid;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : GetProtocolVersion
 * @created : Monday Mar 30, 2020 23:22:44 EDT
 */
public class InitializeInterface implements ARawHidCommand<Integer> {
  @Override
  public byte code() {
    return 0;
  }

  @Override
  public void data(byte[] buffer) {
    buffer[0] = 0x3f;
    buffer[1] = 0x23;
    buffer[2] = 0x23;
    buffer[3] = 0x00;
  }

  @Override
  public Integer response(byte[] data) {
    return 1;
  }
}
