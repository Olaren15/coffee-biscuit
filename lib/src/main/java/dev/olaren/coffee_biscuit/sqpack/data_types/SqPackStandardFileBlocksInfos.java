package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;

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

  public static List<SqPackStandardFileBlocksInfos> fromBytes(
      MemorySegment buffer, long baseOffset, int count) {
    var blockInfos = new ArrayList<SqPackStandardFileBlocksInfos>(count);

    for (int i = 0; i < count; i++) {
      long offset = baseOffset + LAYOUT.byteSize() * i;
      blockInfos.add(fromBytes(buffer, offset));
    }

    return blockInfos;
  }
}
