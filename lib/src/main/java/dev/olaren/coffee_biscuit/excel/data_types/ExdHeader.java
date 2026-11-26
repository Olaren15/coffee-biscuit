package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import dev.olaren.coffee_biscuit.sqpack.InvalidFormatException;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;

public record ExdHeader(byte[] magic, int version, long indexSize) {
  private static final int MAGIC_LENGTH = 4;
  private static final byte[] EXPECTED_MAGIC = {'E', 'X', 'D', 'F'};

  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              MemoryLayout.sequenceLayout(
                      MAGIC_LENGTH, ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN))
                  .withName("magic"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("version"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("unknown1"),
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("indexSize"),
              MemoryLayout.sequenceLayout(5, ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN))
                  .withName("unknown2"))
          .withName("ExdHeader");

  private static final long MAGIC_OFFSET = LAYOUT.byteOffset(groupElement("magic"));
  private static final VarHandle VERSION = LAYOUT.varHandle(groupElement("version"));
  private static final VarHandle INDEX_SIZE = LAYOUT.varHandle(groupElement("indexSize"));

  public static ExdHeader fromBytes(MemorySegment buffer, long baseOffset)
      throws InvalidFormatException {
    byte[] magic = buffer.asSlice(MAGIC_OFFSET, MAGIC_LENGTH).toArray(ValueLayout.JAVA_BYTE);
    if (!Arrays.equals(EXPECTED_MAGIC, magic)) {
      throw new InvalidFormatException("This is not an excel data file");
    }

    return new ExdHeader(
        magic,
        Short.toUnsignedInt((short) VERSION.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) INDEX_SIZE.get(buffer, baseOffset)));
  }
}
