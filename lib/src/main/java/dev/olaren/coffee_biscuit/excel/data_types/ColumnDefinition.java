package dev.olaren.coffee_biscuit.excel.data_types;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public record ColumnDefinition(ColumnDataType type, int offset) {
  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("type"),
              ValueLayout.JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("offset"))
          .withName("ColumnDefinition");

  private static final VarHandle TYPE = LAYOUT.varHandle(groupElement("type"));
  private static final VarHandle OFFSET = LAYOUT.varHandle(groupElement("offset"));

  public static ColumnDefinition fromBytes(MemorySegment buffer, long baseOffset) {
    int typeId = Short.toUnsignedInt((short) TYPE.get(buffer, baseOffset));
    var type =
        ColumnDataType.fromId(typeId)
            .orElseThrow(
                () ->
                    new UnsupportedOperationException(
                        "Unknown excel column data type %x".formatted(typeId)));

    return new ColumnDefinition(type, Short.toUnsignedInt((short) OFFSET.get(buffer, baseOffset)));
  }
}
