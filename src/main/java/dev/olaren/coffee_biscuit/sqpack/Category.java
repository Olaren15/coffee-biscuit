package dev.olaren.coffee_biscuit.sqpack;

import java.util.Arrays;
import java.util.Optional;

public enum Category {
  COMMON((byte) 0x0),
  BG_COMMON((byte) 0x1),
  BG((byte) 0x2),
  CUT((byte) 0x03),
  CHARA((byte) 0x04),
  SHADER((byte) 0x05),
  UI((byte) 0x06),
  SOUND((byte) 0x07),
  VFX((byte) 0x08),
  UI_SCRIPT((byte) 0x09),
  EXD((byte) 0x0A),
  GAME_SCRIPT((byte) 0x0B),
  MUSIC((byte) 0x0C),
  SQPACK_TEST((byte) 0x12),
  DEBUG((byte) 0x13);

  private final byte id;

  Category(byte id) {
    this.id = id;
  }

  public static Optional<Category> fromId(byte id) {
    return Arrays.stream(Category.values()).filter(cat -> cat.id == id).findFirst();
  }

  public static Optional<Category> fromName(String name) {
    return switch (name) {
      case "common" -> Optional.of(COMMON);
      case "bgcommon" -> Optional.of(BG_COMMON);
      case "bg" -> Optional.of(BG);
      case "cut" -> Optional.of(CUT);
      case "chara" -> Optional.of(CHARA);
      case "shader" -> Optional.of(SHADER);
      case "ui" -> Optional.of(UI);
      case "sound" -> Optional.of(SOUND);
      case "vfx" -> Optional.of(VFX);
      case "ui_script" -> Optional.of(UI_SCRIPT);
      case "exd" -> Optional.of(EXD);
      case "game_script" -> Optional.of(GAME_SCRIPT);
      case "music" -> Optional.of(MUSIC);
      case "sqpack_test" -> Optional.of(SQPACK_TEST);
      case "debug" -> Optional.of(DEBUG);
      default -> Optional.empty();
    };
  }
}
