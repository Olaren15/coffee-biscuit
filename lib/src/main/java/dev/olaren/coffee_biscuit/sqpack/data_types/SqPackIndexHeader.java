package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record SqPackIndexHeader(long size, long type, long indexDataOffset, long indexDataSize) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withName("size"),
              ValueLayout.JAVA_INT.withName("type"),
              ValueLayout.JAVA_INT.withName("indexDataOffset"),
              ValueLayout.JAVA_INT.withName("indexDataSize"))
          .withName("SqPackIndexHeader");

  private static final VarHandle SIZE = LAYOUT.varHandle(groupElement("size"));
  private static final VarHandle TYPE = LAYOUT.varHandle(groupElement("type"));
  private static final VarHandle INDEX_DATA_OFFSET =
      LAYOUT.varHandle(groupElement("indexDataOffset"));
  private static final VarHandle INDEX_DATA_SIZE = LAYOUT.varHandle(groupElement("indexDataSize"));

  public static SqPackIndexHeader fromBytes(MemorySegment buffer, long baseOffset) {
    return new SqPackIndexHeader(
        Integer.toUnsignedLong((int) SIZE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) TYPE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) INDEX_DATA_OFFSET.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) INDEX_DATA_SIZE.get(buffer, baseOffset)));
  }
}
