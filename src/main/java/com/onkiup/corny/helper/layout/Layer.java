package com.onkiup.corny.helper.layout;

import com.onkiup.linker.grammars.commons.strings.ACommonString;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Layer
 * @created : Sunday Mar 29, 2020 22:01:41 EDT
 */

public class Layer implements LayoutElement {
  private ACommonString name;
  private static final String OBRA = "{";
  private Row[] rows;
  private static final String CBRA = "}";

  public String name() {
    return name.toString();
  }

  public Row[] rows() {
    return rows;
  }
}
