package dev.olaren.coffee_biscuit.sqpack;

import java.util.Optional;

public enum Category {
  COMMON,
  BG_COMMON,
  BG,
  CUT,
  CHARA,
  SHADER,
  UI,
  SOUND,
  VFX,
  UI_SCRIPT,
  EXD,
  GAME_SCRIPT,
  MUSIC,
  SQPACK_TEST,
  DEBUG;

  public static Optional<Category> fromId(byte id) {
    return switch (id) {
      case 0x0 -> Optional.of(COMMON);
      case 0x1 -> Optional.of(BG_COMMON);
      case 0x2 -> Optional.of(BG);
      case 0x3 -> Optional.of(CUT);
      case 0x4 -> Optional.of(CHARA);
      case 0x5 -> Optional.of(SHADER);
      case 0x6 -> Optional.of(UI);
      case 0x7 -> Optional.of(SOUND);
      case 0x8 -> Optional.of(VFX);
      case 0x9 -> Optional.of(UI_SCRIPT);
      case 0xA -> Optional.of(EXD);
      case 0xB -> Optional.of(GAME_SCRIPT);
      case 0xC -> Optional.of(MUSIC);
      case 0x12 -> Optional.of(SQPACK_TEST);
      case 0x13 -> Optional.of(DEBUG);
      default -> Optional.empty();
    };
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
