package dev.olaren.coffee_biscuit.sqpack.data_types;

import java.util.Optional;

public enum Platform {
  Win32,
  PS3,
  PS4,
  PS5,
  Lys, // Xbox
  Obe; // Switch 2

  public static Optional<Platform> fromId(byte id) {
    return switch (id) {
      case 0x0 -> Optional.of(Win32);
      case 0x1 -> Optional.of(PS3);
      case 0x2 -> Optional.of(PS4);
      case 0x3 -> Optional.of(PS5);
      case 0x4 -> Optional.of(Lys);
      case 0x5 -> Optional.of(Obe);
      default -> Optional.empty();
    };
  }
}
