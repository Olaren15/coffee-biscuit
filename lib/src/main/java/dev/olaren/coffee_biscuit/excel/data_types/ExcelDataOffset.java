package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public record ExcelDataOffset(long rowId, long offset) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("rowId"),
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("offset"))
          .withName("ExcelDataOffset");

  private static final VarHandle ROW_ID = LAYOUT.varHandle(groupElement("rowId"));
  private static final VarHandle OFFSET = LAYOUT.varHandle(groupElement("offset"));

  public static ExcelDataOffset fromBytes(MemorySegment buffer, long baseOffset) {
    return new ExcelDataOffset(
        Integer.toUnsignedLong((int) ROW_ID.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) OFFSET.get(buffer, baseOffset)));
  }
}
