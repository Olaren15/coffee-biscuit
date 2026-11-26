package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import dev.olaren.coffee_biscuit.sqpack.InvalidFormatException;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;

public record ExhHeader(
    byte[] magic,
    int dataOffset,
    int columnCount,
    int pageCount,
    int languageCount,
    Variant variant,
    long rowCount) {
  private static final int MAGIC_LENGTH = 4;
  private static final byte[] EXPECTED_MAGIC = {'E', 'X', 'H', 'F'};

  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              MemoryLayout.sequenceLayout(
                      MAGIC_LENGTH, ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN))
                  .withName("magic"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("unknown0"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("dataOffset"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("columnCount"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("pageCount"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("languageCount"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("unknown1"),
              ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN).withName("unknown2"),
              ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN).withName("variant"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("unknown3"),
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("rowCount"),
              MemoryLayout.sequenceLayout(2, ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN))
                  .withName("unknown4"))
          .withName("ExhHeader");

  private static final long MAGIC_OFFSET = LAYOUT.byteOffset(groupElement("magic"));
  private static final VarHandle DATA_OFFSET = LAYOUT.varHandle(groupElement("dataOffset"));
  private static final VarHandle COLUMN_COUNT = LAYOUT.varHandle(groupElement("columnCount"));
  private static final VarHandle PAGE_COUNT = LAYOUT.varHandle(groupElement("pageCount"));
  private static final VarHandle LANGUAGE_COUNT = LAYOUT.varHandle(groupElement("languageCount"));
  private static final VarHandle VARIANT = LAYOUT.varHandle(groupElement("variant"));
  private static final VarHandle ROW_COUNT = LAYOUT.varHandle(groupElement("rowCount"));

  public static ExhHeader fromBytes(MemorySegment buffer, long baseOffset)
      throws InvalidFormatException {
    byte[] magic = buffer.asSlice(MAGIC_OFFSET, MAGIC_LENGTH).toArray(ValueLayout.JAVA_BYTE);
    if (!Arrays.equals(EXPECTED_MAGIC, magic)) {
      throw new InvalidFormatException("This is not an excel header file");
    }

    int dataOffset = Short.toUnsignedInt((short) DATA_OFFSET.get(buffer, baseOffset));
    int columnCount = Short.toUnsignedInt((short) COLUMN_COUNT.get(buffer, baseOffset));
    int pageCount = Short.toUnsignedInt((short) PAGE_COUNT.get(buffer, baseOffset));
    int languageCount = Short.toUnsignedInt((short) LANGUAGE_COUNT.get(buffer, baseOffset));

    byte variantId = (byte) VARIANT.get(buffer, baseOffset);
    Variant variant =
        Variant.fromId(variantId)
            .orElseThrow(
                () ->
                    new UnsupportedOperationException(
                        "Unknown excel variant %x".formatted(variantId)));

    return new ExhHeader(
        magic,
        dataOffset,
        columnCount,
        pageCount,
        languageCount,
        variant,
        Integer.toUnsignedLong((int) ROW_COUNT.get(buffer, baseOffset)));
  }
}
