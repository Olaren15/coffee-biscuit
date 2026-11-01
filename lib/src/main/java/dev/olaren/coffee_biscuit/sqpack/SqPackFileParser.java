package dev.olaren.coffee_biscuit.sqpack;

import java.lang.foreign.MemorySegment;

@FunctionalInterface
public interface SqPackFileParser<T> {
  T fromBytes(MemorySegment data);
}
