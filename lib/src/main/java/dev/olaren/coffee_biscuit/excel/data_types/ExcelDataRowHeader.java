package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public record ExcelDataRowHeader(long dataSize, int rowCount) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("dataSize"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("rowCount"))
          .withName("ExcelDataRowHeader");

  private static final VarHandle DATA_SIZE = LAYOUT.varHandle(groupElement("dataSize"));
  private static final VarHandle ROW_COUNT = LAYOUT.varHandle(groupElement("rowCount"));

  public static ExcelDataRowHeader fromBytes(MemorySegment buffer, long baseOffset) {
    return new ExcelDataRowHeader(
        Integer.toUnsignedLong((int) DATA_SIZE.get(buffer, baseOffset)),
        Short.toUnsignedInt((short) ROW_COUNT.get(buffer, baseOffset)));
  }
}
