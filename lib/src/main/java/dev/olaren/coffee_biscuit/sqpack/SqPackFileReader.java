package dev.olaren.coffee_biscuit.sqpack;

import dev.olaren.coffee_biscuit.sqpack.data_types.*;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class SqPackFileReader {
  public static <T> T read(MemorySegment readBuf, long baseOffset, SqPackFileParser<T> parser)
      throws IOException {
    var fileInfo = SqPackFileInfo.fromBytes(readBuf, baseOffset);

    if (!SqPackFileType.STANDARD.equals(fileInfo.fileType())) {
      throw new UnsupportedOperationException(
          "filetype %s is not supported".formatted(fileInfo.fileType()));
    }

    var blockInfos =
        SqPackStandardFileBlocksInfos.fromBytes(
            readBuf, baseOffset + SqPackFileInfo.LAYOUT.byteSize(), fileInfo.blockCount());

    try (Arena arena = Arena.ofConfined()) {
      MemorySegment writeBuf = arena.allocate(fileInfo.decompressedSize());
      long writePos = 0;

      for (SqPackStandardFileBlocksInfos blockInfo : blockInfos) {
        long blockOffset = baseOffset + fileInfo.headerSize() + blockInfo.offset();
        writePos += readBlock(readBuf, blockOffset, writeBuf, writePos);
      }

      return parser.fromBytes(writeBuf);
    }
  }

  private static long readBlock(
      MemorySegment readBuf, long blockOffset, MemorySegment writeBuf, long writePos)
      throws IOException {
    var blockHeader = SqPackFileBlockHeader.fromBytes(readBuf, blockOffset);

    if (blockHeader.compressedSize() == blockHeader.decompressedSize()) {
      MemorySegment.copy(
          readBuf,
          blockOffset + blockHeader.size(),
          writeBuf,
          writePos,
          blockHeader.decompressedSize());
    } else {
      inflateBlock(
          readBuf,
          blockOffset + blockHeader.size(),
          blockHeader.compressedSize(),
          writeBuf,
          writePos,
          blockHeader.decompressedSize());
    }

    return blockHeader.decompressedSize();
  }

  private static void inflateBlock(
      MemorySegment readBuf,
      long readOffset,
      long readSize,
      MemorySegment writeBuf,
      long writePos,
      long writeSize)
      throws IOException {
    try (Inflater inflater = new Inflater(true)) {
      ByteBuffer compressedBuffer = readBuf.asSlice(readOffset, readSize).asByteBuffer();
      ByteBuffer inflatedBuffer = writeBuf.asSlice(writePos, writeSize).asByteBuffer();

      inflater.setInput(compressedBuffer);
      inflater.inflate(inflatedBuffer);

      if (!inflater.finished()) {
        throw new IOException("Failed to properly inflate block");
      }
    } catch (DataFormatException e) {
      throw new IOException(e);
    }
  }
}
