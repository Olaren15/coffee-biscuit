package dev.olaren.coffee_biscuit.sqpack.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record SqPackFileInfo(
    long headerSize, SqPackFileType fileType, long decompressedSize, long blockCount) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_INT.withName("headerSize"),
              ValueLayout.JAVA_INT.withName("fileType"),
              ValueLayout.JAVA_INT.withName("decompressedSize"),
              ValueLayout.JAVA_INT.withName("unknown1"),
              ValueLayout.JAVA_INT.withName("unknown2"),
              ValueLayout.JAVA_INT.withName("blockCount"))
          .withName("SqPackFileInfo");

  private static final VarHandle HEADER_SIZE = LAYOUT.varHandle(groupElement("headerSize"));
  private static final VarHandle FILE_TYPE = LAYOUT.varHandle(groupElement("fileType"));
  private static final VarHandle DECOMPRESSED_SIZE =
      LAYOUT.varHandle(groupElement("decompressedSize"));
  private static final VarHandle BLOCK_COUNT = LAYOUT.varHandle(groupElement("blockCount"));

  public static SqPackFileInfo fromBytes(MemorySegment buffer, long baseOffset) {
    var size = Integer.toUnsignedLong((int) HEADER_SIZE.get(buffer, baseOffset));

    long fileTypeId = Integer.toUnsignedLong((int) FILE_TYPE.get(buffer, baseOffset));
    var fileType =
        SqPackFileType.fromId(fileTypeId)
            .orElseThrow(
                () ->
                    new UnsupportedOperationException(
                        "Unknown file type id %d".formatted(fileTypeId)));

    return new SqPackFileInfo(
        size,
        fileType,
        Integer.toUnsignedLong((int) DECOMPRESSED_SIZE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) BLOCK_COUNT.get(buffer, baseOffset)));
  }
}
