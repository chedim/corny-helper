package com.onkiup.corny.helper.layout;

import com.onkiup.corny.helper.arguments.CornyConnector;
import com.onkiup.linker.parser.Rule;
import com.onkiup.linker.parser.annotation.CapturePattern;
import com.onkiup.linker.parser.annotation.OptionalToken;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Key
 * @created : Sunday Mar 29, 2020 21:41:52 EDT
 */

public class Key implements LayoutElement {
  private static final String OBRA = "[";
  private Definition[] definitions;
  private static final String CBRA = "]";

  public Definition[] definitions() {
    return definitions;
  }

  public static class Definition implements Rule {
    private Code code;
    @OptionalToken
    private static final String MARKER = "|";

    public Code code() {
      return code;
    }
  }

  public interface Code extends Rule {

  }

  public static class StandardCode implements Code {
    @CapturePattern(until="[~\\\\\\|\\]]")
    private String code;

    public String code() {
      return code;
    }
  }

  public static class LayerCode implements Code {
    private static final String MARKER = "~";
    @CapturePattern(until="[\\|\\]]")
    private String layer;

    public String layer() {
      return layer;
    }
  }
}

