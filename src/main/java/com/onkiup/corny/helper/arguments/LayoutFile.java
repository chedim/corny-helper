package com.onkiup.corny.helper.arguments;

import java.io.FileReader;

import com.onkiup.corny.helper.layout.Layout;
import com.onkiup.linker.grammars.commons.strings.ACommonString;
import com.onkiup.linker.parser.TokenGrammar;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : LayoutFile
 * @created : Sunday Mar 29, 2020 22:08:47 EDT
 */

public class LayoutFile implements AnArgument {
  private static final String MARKER = "--layout=";
  private ACommonString source;

  private transient String cache;
  private transient Layout result;

  public LayoutFile() {
  }

  public LayoutFile(String source) {
    this.cache = source;
  }

  public String source() {
    if (cache == null) {
      cache = source.toString();
    }
    return cache;
  }

  public Layout read() {
    if (result == null) {
      try {
        result = TokenGrammar.forClass(Layout.class).parse(new FileReader(source()));
      } catch (Exception e) {
        throw new RuntimeException("Failed to load layout from '" + source() + "'", e);
      }
    }

    return result;
  }

}
