package dev.olaren.coffee_biscuit.excel;

import java.util.Optional;

public enum Language {
  NONE,
  JAPANESE,
  ENGLISH,
  GERMAN,
  FRENCH,
  CHINESE_SIMPLIFIED,
  CHINESE_TRADITIONAL,
  KOREAN,
  TRADITIONAL_CHINESE;

  public static Optional<Language> fromId(int id) {
    return switch (id) {
      case 0x0 -> Optional.of(NONE);
      case 0x1 -> Optional.of(JAPANESE);
      case 0x2 -> Optional.of(ENGLISH);
      case 0x3 -> Optional.of(GERMAN);
      case 0x4 -> Optional.of(FRENCH);
      case 0x5 -> Optional.of(CHINESE_SIMPLIFIED);
      case 0x6 -> Optional.of(CHINESE_TRADITIONAL);
      case 0x7 -> Optional.of(KOREAN);
      case 0x8 -> Optional.of(TRADITIONAL_CHINESE);
      default -> Optional.empty();
    };
  }

  public String getCode() {
    return switch (this) {
      case NONE -> "";
      case JAPANESE -> "ja";
      case ENGLISH -> "en";
      case GERMAN -> "de";
      case FRENCH -> "fr";
      case CHINESE_SIMPLIFIED -> "chs";
      case CHINESE_TRADITIONAL -> "cht";
      case KOREAN -> "ko";
      case TRADITIONAL_CHINESE -> "tc";
    };
  }
}
