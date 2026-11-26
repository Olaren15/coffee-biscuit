package dev.olaren.coffee_biscuit.excel;

import dev.olaren.coffee_biscuit.excel.data_types.ExcelDataOffset;
import dev.olaren.coffee_biscuit.excel.data_types.ExcelDataRowHeader;
import dev.olaren.coffee_biscuit.excel.data_types.ExdHeader;
import dev.olaren.coffee_biscuit.sqpack.InvalidFormatException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Page<T extends SheetItem> implements AutoCloseable {
  private final Arena arena;
  private final MemorySegment data;

  private final Map<Long, Long> index;

  private Page(Arena arena, MemorySegment data, Map<Long, Long> index) {
    this.arena = arena;
    this.data = data;
    this.index = index;
  }

  public static <T extends SheetItem> Page<T> fromBytes(MemorySegment buffer)
      throws InvalidFormatException {
    var arena = Arena.ofShared();
    var data = arena.allocate(buffer.byteSize());
    data.copyFrom(buffer).asReadOnly();

    var header = ExdHeader.fromBytes(data, 0);

    long offsetCount = header.indexSize() / ExcelDataOffset.LAYOUT.byteSize();
    var index = new HashMap<Long, Long>((int) offsetCount);
    for (int i = 0; i < offsetCount; i++) {
      long offset = ExdHeader.LAYOUT.byteSize() + i * ExcelDataOffset.LAYOUT.byteSize();
      var entry = ExcelDataOffset.fromBytes(data, offset);

      index.put(entry.rowId(), entry.offset());
    }

    return new Page<>(arena, data, index);
  }

  public boolean containsRow(long id) {
    return index.containsKey(id);
  }

  public T getRow(Class<T> clazz, long id, long baseVariableDataOffset) {
    Long rowOffset = index.get(id);
    if (rowOffset == null) {
      throw new NoSuchElementException("Cannot find row with id %d".formatted(id));
    }

    var header = ExcelDataRowHeader.fromBytes(data, rowOffset);
    long fixedDataOffset = rowOffset + ExcelDataRowHeader.LAYOUT.byteSize();
    long variableDataOffset = fixedDataOffset + baseVariableDataOffset;

    return instantiateRow(clazz, id, fixedDataOffset, variableDataOffset);
  }

  private T instantiateRow(Class<T> clazz, long id, long fixedDataOffset, long variableDataOffset) {
    try {
      return clazz
          .getDeclaredConstructor(Page.class, long.class, long.class, long.class)
          .newInstance(this, id, fixedDataOffset, variableDataOffset);
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public Stream<T> getRows(Class<T> clazz, long baseVariableDataOffset) {
    return index.keySet().stream().map(id -> getRow(clazz, id, baseVariableDataOffset));
  }

  public String readString(long offset, long variableDataOffset) {
    long realOffset = readUInt32(offset) + variableDataOffset;

    return data.getString(realOffset, StandardCharsets.UTF_8);
  }

  public boolean readBool(long offset) {
    return data.get(ValueLayout.JAVA_BOOLEAN.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  public byte readInt8(long offset) {
    return data.get(ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  public int readUInt8(long offset) {
    return Byte.toUnsignedInt(
        data.get(ValueLayout.JAVA_BYTE.withOrder(ByteOrder.BIG_ENDIAN), offset));
  }

  public short readInt16(long offset) {
    return data.get(ValueLayout.JAVA_SHORT_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  public int readUInt16(long offset) {
    return Short.toUnsignedInt(
        data.get(ValueLayout.JAVA_SHORT_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset));
  }

  public int readInt32(long offset) {
    return data.get(ValueLayout.JAVA_INT_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  public long readUInt32(long offset) {
    return Integer.toUnsignedLong(
        data.get(ValueLayout.JAVA_INT_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset));
  }

  public float readFloat32(long offset) {
    return data.get(ValueLayout.JAVA_FLOAT_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  public long readInt64(long offset) {
    return data.get(ValueLayout.JAVA_LONG_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset);
  }

  // i hate java
  public BigInteger readUInt64(long offset) {
    long raw = data.get(ValueLayout.JAVA_LONG_UNALIGNED.withOrder(ByteOrder.BIG_ENDIAN), offset);
    return new BigInteger(Long.toUnsignedString(raw));
  }

  public boolean readPackedBool(long offset, int shift) {
    return false;
  }

  @Override
  public void close() {
    arena.close();
  }
}
