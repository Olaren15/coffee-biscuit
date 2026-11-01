package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record SqPackFileBlockHeader(long size, long compressedSize, long decompressedSize) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withName("size"),
              ValueLayout.JAVA_INT.withName("unknown0"),
              ValueLayout.JAVA_INT.withName("compressedSize"),
              ValueLayout.JAVA_INT.withName("decompressedSize"))
          .withName("SqPackFileBlockHeader");

  private static final VarHandle SIZE = LAYOUT.varHandle(groupElement("size"));
  private static final VarHandle COMPRESSED_SIZE = LAYOUT.varHandle(groupElement("compressedSize"));
  private static final VarHandle DECOMPRESSED_SIZE =
      LAYOUT.varHandle(groupElement("decompressedSize"));

  public static SqPackFileBlockHeader fromBytes(MemorySegment buffer, long baseOffset) {

    return new SqPackFileBlockHeader(
        Integer.toUnsignedLong((int) SIZE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) COMPRESSED_SIZE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) DECOMPRESSED_SIZE.get(buffer, baseOffset)));
  }
}
