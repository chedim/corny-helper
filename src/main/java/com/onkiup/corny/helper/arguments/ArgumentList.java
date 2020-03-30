package com.onkiup.corny.helper.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.onkiup.linker.parser.Rule;

/**
 * @author : chedim (chedim@chedim-Surface-Pro-3)
 * @file : ArgumentList
 * @created : Saturday Mar 28, 2020 01:42:39 EDT
 */

public class ArgumentList implements Rule {
  private AnArgument[] arguments;
  private transient Map<Class, List<AnArgument>> byType;

  private void init() {
    if (byType == null) {
      byType = new HashMap<>();
      if (arguments != null) {
        Arrays.stream(arguments).forEach(this::addAnArgument);
      }
    }
  }

  private void addAnArgument(AnArgument argument) {
    init();
    Class type = argument.getClass();
    if (!byType.containsKey(type)) {
      byType.put(type, new LinkedList<>());
    }
    byType.get(type).add(argument);
  }

  public <X extends AnArgument> List<X> getAll(Class<X> ofType) {
    init();
    return (List<X>) byType.get(ofType);
  }

  public <X extends AnArgument> Optional<X> getSingle(Class<X> ofType) {
    Collection<X> all = getAll(ofType);
    if (all == null || all.size() == 0) {
      return Optional.empty();
    } else if (all != null && all.size() > 1) {
      throw new RuntimeException(
          "Requested 'single' instance of " + ofType.getCanonicalName() + " but there were " + all.size());
    }

    return Optional.of(all.iterator().next());
  }

  public <X extends AnArgument> Optional<X> getLast(Class<X> ofType) {
    List<X> all = getAll(ofType);
    if (all.size() == 0) {
      return Optional.empty();
    }
    return Optional.of(all.get(all.size() - 1));
  }
}
