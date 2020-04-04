package com.onkiup.corny.helper.rawhid;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : ACommand
 * @created : Monday Mar 30, 2020 23:20:43 EDT
 */

public interface ARawHidCommand<R> {
  byte code();
  void data(byte[] buffer);
  R response(byte[] data);
}
