package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record SqPackStandardFileBlocksInfos(long offset, int compressedSize, int decompressedSize) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withName("offset"),
              ValueLayout.JAVA_SHORT.withName("compressedSize"),
              ValueLayout.JAVA_SHORT.withName("decompressedSize"))
          .withName("SqPackStandardFileBlocksHeader");

  private static final VarHandle OFFSET = LAYOUT.varHandle(groupElement("offset"));
  private static final VarHandle COMPRESSED_SIZE = LAYOUT.varHandle(groupElement("compressedSize"));
  private static final VarHandle DECOMPRESSED_SIZE =
      LAYOUT.varHandle(groupElement("decompressedSize"));

  public static SqPackStandardFileBlocksInfos fromBytes(MemorySegment buffer, long baseOffset) {
    return new SqPackStandardFileBlocksInfos(
        Integer.toUnsignedLong((int) OFFSET.get(buffer, baseOffset)),
        Short.toUnsignedInt((short) COMPRESSED_SIZE.get(buffer, baseOffset)),
        Short.toUnsignedInt((short) DECOMPRESSED_SIZE.get(buffer, baseOffset)));
  }

  public static SqPackStandardFileBlocksInfos[] fromBytes(
      MemorySegment buffer, long baseOffset, long count) {
    var blockInfos = new SqPackStandardFileBlocksInfos[(int) count];

    for (int i = 0; i < blockInfos.length; i++) {
      long offset = baseOffset + SqPackStandardFileBlocksInfos.LAYOUT.byteSize() * i;
      blockInfos[i] = SqPackStandardFileBlocksInfos.fromBytes(buffer, offset);
    }

    return blockInfos;
  }
}
