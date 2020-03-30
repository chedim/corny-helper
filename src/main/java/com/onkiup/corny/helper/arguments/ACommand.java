package com.onkiup.corny.helper.arguments;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : ACommand
 * @created : Saturday Mar 28, 2020 23:53:21 EDT
 */

public interface ACommand extends AnArgument {
  int execute(ArgumentList arguments);
}
