package dev.olaren.coffee_biscuit.sqpack;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class RawSqPackFile implements AutoCloseable {
  private final Arena arena;
  private final MemorySegment data;

  private RawSqPackFile(Arena arena, MemorySegment data) {
    this.arena = arena;
    this.data = data;
  }

  public static RawSqPackFile fromBytes(MemorySegment data) {
    var arena = Arena.ofShared();

    var copiedData = arena.allocate(data.byteSize());
    copiedData.copyFrom(data).asReadOnly();

    return new RawSqPackFile(arena, copiedData);
  }

  public MemorySegment data() {
    return data;
  }

  public void close() {
    arena.close();
  }
}
