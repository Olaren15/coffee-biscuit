package dev.olaren.coffee_biscuit.excel;

import dev.olaren.coffee_biscuit.memory.SegmentLinesIterator;
import dev.olaren.coffee_biscuit.sqpack.InvalidFormatException;
import dev.olaren.coffee_biscuit.sqpack.SqPackPath;
import java.lang.foreign.MemorySegment;
import java.util.*;

public class ExcelList {
  private final Map<String, SheetProps> namesToProps;

  private ExcelList(Map<String, SheetProps> namesToProps) {
    this.namesToProps = namesToProps;
  }

  public static ExcelList fromBytes(MemorySegment data) throws InvalidFormatException {
    SegmentLinesIterator it = new SegmentLinesIterator(data);

    String header = it.next();
    if (!"EXLT,2".equals(header)) {
      throw new InvalidFormatException("This file is not an Excel list");
    }

    Map<String, SheetProps> nameToIds = new HashMap<>();
    while (it.hasNext()) {
      String line = it.next();
      String[] segments = line.split(",");

      if (segments.length != 2) {
        throw new InvalidFormatException("Encountered unexpected data while reading Excel List");
      }

      String nameLower = segments[0].toLowerCase(Locale.ROOT);
      int id = Integer.parseInt(segments[1]);
      var headerPath = SqPackPath.fromString("exd/%s.exh".formatted(nameLower));
      var props = new SheetProps(id, headerPath);

      nameToIds.put(nameLower, props);
    }

    return new ExcelList(Collections.unmodifiableMap(nameToIds));
  }

  public Set<Map.Entry<String, SheetProps>> getSheetsProps() {
    return namesToProps.entrySet();
  }

  public Optional<SheetProps> getSheetProps(String name) {
    return Optional.ofNullable(namesToProps.get(name));
  }

  public boolean sheetExists(String name) {
    return this.namesToProps.containsKey(name);
  }

  public record SheetProps(int id, SqPackPath headerPath) {}
}
