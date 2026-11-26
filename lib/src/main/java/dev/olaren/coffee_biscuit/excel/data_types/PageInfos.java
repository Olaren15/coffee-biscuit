package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public record PageInfos(long startId, long rowCount) {
  public static MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("startId"),
              ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("rowCount"))
          .withName("PageInfos");

  private static final VarHandle START_ID = LAYOUT.varHandle(groupElement("startId"));
  private static final VarHandle ROW_COUNT = LAYOUT.varHandle(groupElement("rowCount"));

  public static PageInfos fromBytes(MemorySegment buffer, long baseOffset) {
    return new PageInfos(
        Integer.toUnsignedLong((int) START_ID.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) ROW_COUNT.get(buffer, baseOffset)));
  }
}
