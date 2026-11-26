package dev.olaren.coffee_biscuit.memory;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SegmentLinesIterator implements Iterator<String> {
  private final MemorySegment segment;
  private long pos;

  public SegmentLinesIterator(MemorySegment segment) {
    this.segment = segment;
    this.pos = 0;
  }

  @Override
  public boolean hasNext() {
    return pos < segment.byteSize();
  }

  @Override
  public String next() {
    if (pos >= segment.byteSize()) {
      throw new IndexOutOfBoundsException("Tried to read further than the segment size");
    }

    long start = pos;
    long end = start;
    while (end < segment.byteSize() && segment.get(ValueLayout.JAVA_BYTE, end) != '\n') {
      end++;
    }

    // Skip newline
    pos = end + 1;

    long lineEnd = end;
    if (lineEnd > start && segment.get(ValueLayout.JAVA_BYTE, lineEnd - 1) == '\r') {
      // Account for \r\n
      lineEnd--;
    }

    byte[] bytes = segment.asSlice(start, lineEnd - start).toArray(ValueLayout.JAVA_BYTE);
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
