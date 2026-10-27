package dev.olaren.coffee_biscuit.sqpack;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;
import static java.lang.foreign.MemoryLayout.PathElement.sequenceElement;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public record SqPackHeader(
    byte[] magic, PlatformId platformId, long size, long version, long type) {
  private static final int MAGIC_LENGTH = 8;

  public static final MemoryLayout LAYOUT =
      MemoryLayout.structLayout(
              MemoryLayout.sequenceLayout(MAGIC_LENGTH, ValueLayout.JAVA_BYTE).withName("magic"),
              ValueLayout.JAVA_BYTE.withName("platformId"),
              MemoryLayout.paddingLayout(3),
              ValueLayout.JAVA_INT.withName("size"),
              ValueLayout.JAVA_INT.withName("version"),
              ValueLayout.JAVA_INT.withName("type"))
          .withName("SqPackHeader");

  private static final VarHandle MAGIC = LAYOUT.varHandle(groupElement("magic"), sequenceElement());
  private static final VarHandle PLATFORM_ID = LAYOUT.varHandle(groupElement("platformId"));
  private static final VarHandle SIZE = LAYOUT.varHandle(groupElement("size"));
  private static final VarHandle VERSION = LAYOUT.varHandle(groupElement("version"));
  private static final VarHandle TYPE = LAYOUT.varHandle(groupElement("type"));

  public static SqPackHeader fromBytes(MemorySegment buffer, long baseOffset) {
    var magic = new byte[8];
    for (int i = 0; i < MAGIC_LENGTH; i++) {
      magic[i] = (byte) MAGIC.get(buffer, baseOffset, (long) i);
    }

    return new SqPackHeader(
        magic,
        PlatformId.fromByte((byte) PLATFORM_ID.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) SIZE.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) VERSION.get(buffer, baseOffset)),
        Integer.toUnsignedLong((int) TYPE.get(buffer, baseOffset)));
  }
}
