package dev.olaren.coffee_biscuit.sqpack;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class Index2 {
  private final SqPackHeader header;
  private final SqPackIndexHeader indexHeader;
  private final Map<Long, Index2HashTableEntry> entries;

  private Index2(
      SqPackHeader header, SqPackIndexHeader indexHeader, Map<Long, Index2HashTableEntry> entries) {
    this.header = header;
    this.indexHeader = indexHeader;
    this.entries = entries;
  }

  static Index2 loadFromPath(Path file) throws IOException {
    try (Arena arena = Arena.ofConfined()) {
      MemorySegment buffer;

      try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
        buffer = arena.allocate(channel.size(), 8);
        ByteBuffer view = buffer.asByteBuffer();

        // Since the index files are quite small, it is usually more performant to just load it all
        // at
        // once
        //noinspection StatementWithEmptyBody
        while (view.hasRemaining() && channel.read(view) != -1) {}
      }

      var header = SqPackHeader.fromBytes(buffer, 0);
      var indexHeader = SqPackIndexHeader.fromBytes(buffer, header.size());

      long entrySize = Index2HashTableEntry.LAYOUT.byteSize();
      long count = indexHeader.indexDataSize() / entrySize;
      var entries = new HashMap<Long, Index2HashTableEntry>();

      for (long i = 0; i < count; i++) {
        long offset = indexHeader.indexDataOffset() + (entrySize * i);
        var entry = Index2HashTableEntry.fromBytes(buffer, offset);

        entries.put(entry.hash(), entry);
      }

      return new Index2(header, indexHeader, entries);
    }
  }
}
