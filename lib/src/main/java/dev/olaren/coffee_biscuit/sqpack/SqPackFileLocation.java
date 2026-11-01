package dev.olaren.coffee_biscuit.sqpack;

import dev.olaren.coffee_biscuit.sqpack.data_types.Index2HashTableEntry;
import java.nio.file.Path;

public record SqPackFileLocation(Path filePath, long byteOffset) {
  public static SqPackFileLocation fromIndex2Entry(
      Path index2FilePath, Index2HashTableEntry entry) {
    byte fileId = (byte) ((entry.data() & 0b1110) >> 1);
    long offset = (entry.data() & ~0xF);

    String indexFileName = index2FilePath.getFileName().toString();
    String datFileName = indexFileName.replace(".index2", ".dat%d".formatted(fileId));
    Path datFilePath = index2FilePath.resolveSibling(datFileName);

    return new SqPackFileLocation(datFilePath, offset * 0x08);
  }
}
