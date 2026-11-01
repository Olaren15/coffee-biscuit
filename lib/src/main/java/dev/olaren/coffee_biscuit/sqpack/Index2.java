package dev.olaren.coffee_biscuit.sqpack;

import dev.olaren.coffee_biscuit.sqpack.data_types.Index2HashTableEntry;
import dev.olaren.coffee_biscuit.sqpack.data_types.SqPackHeader;
import dev.olaren.coffee_biscuit.sqpack.data_types.SqPackIndexHeader;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Index2 {
  private final Map<Long, SqPackFileLocation> locationByHash;

  private Index2(Map<Long, SqPackFileLocation> locationByHash) {
    this.locationByHash = locationByHash;
  }

  static Index2 loadFromPath(Path filePath) throws IOException {
    Map<Long, SqPackFileLocation> locations;

    try (Arena arena = Arena.ofConfined()) {
      MemorySegment buffer;

      try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)) {
        buffer = arena.allocate(channel.size(), 8);
        ByteBuffer view = buffer.asByteBuffer();

        // Since the index files are quite small, it is usually more performant to just load it all
        // at once
        //noinspection StatementWithEmptyBody
        while (view.hasRemaining() && channel.read(view) != -1) {}
      }

      var header = SqPackHeader.fromBytes(buffer, 0);
      var indexHeader = SqPackIndexHeader.fromBytes(buffer, header.size());

      long entrySize = Index2HashTableEntry.LAYOUT.byteSize();
      long count = indexHeader.indexDataSize() / entrySize;
      locations = new HashMap<>((int) count);

      for (long i = 0; i < count; i++) {
        long offset = indexHeader.indexDataOffset() + (entrySize * i);
        var entry = Index2HashTableEntry.fromBytes(buffer, offset);
        var location = SqPackFileLocation.fromIndex2Entry(filePath, entry);

        locations.put(entry.hash(), location);
      }
    }

    return new Index2(locations);
  }

  public Optional<SqPackFileLocation> getFileLocation(SqPackPath path) {
    return Optional.ofNullable(locationByHash.get(path.index2Hash()));
  }
}
