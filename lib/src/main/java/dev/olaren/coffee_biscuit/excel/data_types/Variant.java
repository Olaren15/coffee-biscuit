package dev.olaren.coffee_biscuit.excel.data_types;

import java.util.Optional;

public enum Variant {
  UNKNOWN,
  DEFAULT,
  SUBROWS;

  public static Optional<Variant> fromId(byte id) {
    return switch (id) {
      case 0x0 -> Optional.of(UNKNOWN);
      case 0x1 -> Optional.of(DEFAULT);
      case 0x2 -> Optional.of(SUBROWS);
      default -> Optional.empty();
    };
  }
}
