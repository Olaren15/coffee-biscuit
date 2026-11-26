package dev.olaren.coffee_biscuit.excel.data_types;

import java.util.Optional;

public enum ColumnDataType {
  STRING,
  BOOL,
  INT8,
  UINT8,
  INT16,
  UINT16,
  INT32,
  UINT32,
  FLOAT32,
  INT64,
  UINT64,

  PACKEDBOOL0,
  PACKEDBOOL1,
  PACKEDBOOL2,
  PACKEDBOOL3,
  PACKEDBOOL4,
  PACKEDBOOL5,
  PACKEDBOOL6,
  PACKEDBOOL7;

  public static Optional<ColumnDataType> fromId(int id) {
    return switch (id) {
      case 0x0 -> Optional.of(STRING);
      case 0x1 -> Optional.of(BOOL);
      case 0x2 -> Optional.of(INT8);
      case 0x3 -> Optional.of(UINT8);
      case 0x4 -> Optional.of(INT16);
      case 0x5 -> Optional.of(UINT16);
      case 0x6 -> Optional.of(INT32);
      case 0x7 -> Optional.of(UINT32);
      case 0x9 -> Optional.of(FLOAT32);
      case 0xA -> Optional.of(INT64);
      case 0xB -> Optional.of(UINT64);

      case 0x19 -> Optional.of(PACKEDBOOL0);
      case 0x1A -> Optional.of(PACKEDBOOL1);
      case 0x1B -> Optional.of(PACKEDBOOL2);
      case 0x1C -> Optional.of(PACKEDBOOL3);
      case 0x1D -> Optional.of(PACKEDBOOL4);
      case 0x1E -> Optional.of(PACKEDBOOL5);
      case 0x1F -> Optional.of(PACKEDBOOL6);
      case 0x20 -> Optional.of(PACKEDBOOL7);
      default -> Optional.empty();
    };
  }
}
