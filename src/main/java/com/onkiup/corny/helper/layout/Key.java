package com.onkiup.corny.helper.layout;

import java.util.Optional;

import com.onkiup.linker.grammars.commons.strings.ACommonString;
import com.onkiup.linker.parser.Rule;
import com.onkiup.linker.parser.annotation.OptionalToken;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Key
 * @created : Sunday Mar 29, 2020 21:41:52 EDT
 */

public class Key implements LayoutElement {
  private static final String OBRA = "[";
  private ACommonString name;
  @OptionalToken
  private Code code;
  private static final String CBRA = "]";

  public String name() {
    return name.toString();
  }

  public Optional<Integer> code() {
    return Optional.ofNullable(code).map(Code::code);
  }

  public static class Code implements Rule {
    private static final String MARKER = "|";
    private Integer code;

    public Integer code() {
      return code;
    }
  }
}
