package dev.olaren.coffee_biscuit.sqpack.data_types;

import java.util.Optional;

public enum SqPackFileType {
  EMPTY,
  STANDARD,
  MODEL,
  TEXTURE;

  public static Optional<SqPackFileType> fromId(long id) {
    return switch ((int) id) { // this cast will never betray me right?
      case 1 -> Optional.of(EMPTY);
      case 2 -> Optional.of(STANDARD);
      case 3 -> Optional.of(MODEL);
      case 4 -> Optional.of(TEXTURE);
      default -> Optional.empty();
    };
  }
}
