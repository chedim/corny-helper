package com.onkiup.corny.helper.layout;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Row
 * @created : Sunday Mar 29, 2020 21:58:52 EDT
 */

public class Row implements LayoutElement {
  private Key[] keys;
  private static final String EOL = ";";

  public Key[] keys() {
    return keys;
  }
}
