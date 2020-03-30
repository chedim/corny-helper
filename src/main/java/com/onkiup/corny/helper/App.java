package com.onkiup.corny.helper;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.onkiup.corny.helper.arguments.ACommand;
import com.onkiup.corny.helper.arguments.ArgumentList;
import com.onkiup.corny.helper.arguments.commands.Render;
import com.onkiup.linker.parser.TokenGrammar;

public class App {
  private static final Class<? extends ACommand> defaultCommand = Render.class;

  public static void main(String[] args) {
    String arguments = Arrays.stream(args).collect(Collectors.joining(" "));
    ArgumentList parsedArguments;
    if (arguments.length() > 0) {
      parsedArguments = TokenGrammar.forClass(ArgumentList.class).parse(arguments);
    } else {
      parsedArguments = new ArgumentList();
    }
    int result = new App(parsedArguments).execute();
    if (result >= 0) {
      System.exit(result);
    }
  }

  private JFrame frame;
  private JDialog dialog;
  private ArgumentList arguments;

  protected App(ArgumentList args) {
    arguments = args;
  }

  private int execute() {
    return arguments.getSingle(ACommand.class).orElseGet(App::defaultCommand).execute(arguments);
  }

  private static <X extends ACommand> X defaultCommand() {
    try {
      return (X) defaultCommand.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
