package dev.olaren.coffee_biscuit.excel;

import java.lang.foreign.MemorySegment;

@FunctionalInterface
public interface SheetItemParser<T> {
  T fromBytes(MemorySegment buffer, long offset);
}
