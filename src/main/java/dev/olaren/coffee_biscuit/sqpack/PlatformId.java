package dev.olaren.coffee_biscuit.sqpack;

public enum PlatformId {
  Win32,
  PS3,
  PS4,
  PS5,
  Lys, // Xbox
  Obe; // Switch 2

  public static PlatformId fromByte(byte id) {
    return switch (id) {
      case 0x0 -> Win32;
      case 0x1 -> PS3;
      case 0x2 -> PS4;
      case 0x3 -> PS5;
      case 0x4 -> Lys;
      case 0x5 -> Obe;
      default -> throw new IllegalStateException("Unexpected value: " + id);
    };
  }
}
