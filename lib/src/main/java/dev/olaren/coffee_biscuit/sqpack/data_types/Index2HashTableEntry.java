package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record Index2HashTableEntry(long hash, long data) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withName("hash"), ValueLayout.JAVA_INT.withName("data"))
          .withName("SqPackHeader");

  private static final VarHandle HASH = LAYOUT.varHandle(groupElement("hash"));
  private static final VarHandle DATA = LAYOUT.varHandle(groupElement("data"));

  public static Index2HashTableEntry fromBytes(MemorySegment buffer, long baseOffset) {
    long hash = Integer.toUnsignedLong((int) HASH.get(buffer, baseOffset));
    long data = Integer.toUnsignedLong((int) DATA.get(buffer, baseOffset));

    return new Index2HashTableEntry(hash, data);
  }
}
