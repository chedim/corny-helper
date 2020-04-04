package com.onkiup.corny.helper.layout;

import com.onkiup.linker.parser.annotation.CapturePattern;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : Comment
 * @created : Thursday Apr 02, 2020 14:31:52 EDT
 */

public class Comment implements LayoutElement {
  private static final String MARKER = "\n#";
  
  @CapturePattern(until="\n")
  private String comment;

  public String comment() {
    return comment;
  }
}
