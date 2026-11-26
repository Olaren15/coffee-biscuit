package dev.olaren.coffee_biscuit.excel;

import dev.olaren.coffee_biscuit.excel.data_types.ColumnDefinition;
import dev.olaren.coffee_biscuit.excel.data_types.ExhHeader;
import dev.olaren.coffee_biscuit.excel.data_types.PageInfos;
import dev.olaren.coffee_biscuit.sqpack.InvalidFormatException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.ArrayList;
import java.util.List;

public record SheetHeader(
    long baseVariableDataOffset,
    List<ColumnDefinition> columnDefinitions,
    List<PageInfos> pagesInfos,
    List<Language> languages) {

  public static SheetHeader fromBytes(MemorySegment buffer) throws InvalidFormatException {
    var header = ExhHeader.fromBytes(buffer, 0);

    long columnsBaseOffset = ExhHeader.LAYOUT.byteSize();
    List<ColumnDefinition> columnDefinitions =
        extractColumnDefinitions(buffer, header.columnCount(), columnsBaseOffset);

    long paginationBaseOffset =
        columnsBaseOffset + header.columnCount() * ColumnDefinition.LAYOUT.byteSize();
    List<PageInfos> pagesInfos =
        extractPagesInfos(buffer, header.pageCount(), paginationBaseOffset);

    long languagesBaseOffset =
        paginationBaseOffset + header.pageCount() * PageInfos.LAYOUT.byteSize();
    List<Language> languages =
        extractLanguages(buffer, header.languageCount(), languagesBaseOffset);

    return new SheetHeader(header.dataOffset(), columnDefinitions, pagesInfos, languages);
  }

  private static List<ColumnDefinition> extractColumnDefinitions(
      MemorySegment buffer, int count, long columnsBaseOffset) {
    List<ColumnDefinition> columnDefinitions = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      long offset = columnsBaseOffset + i * ColumnDefinition.LAYOUT.byteSize();
      var column = ColumnDefinition.fromBytes(buffer, offset);

      columnDefinitions.add(column);
    }

    return columnDefinitions;
  }

  private static List<PageInfos> extractPagesInfos(
      MemorySegment buffer, int count, long paginationBaseOffset) {
    List<PageInfos> pagesInfos = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      long offset = paginationBaseOffset + i * PageInfos.LAYOUT.byteSize();
      var pageInfos = PageInfos.fromBytes(buffer, offset);

      pagesInfos.add(pageInfos);
    }
    return pagesInfos;
  }

  private static List<Language> extractLanguages(MemorySegment buffer, int count, long baseOffset) {
    List<Language> languages = new ArrayList<>(count);

    for (int i = 0; i < count; i++) {
      long offset = baseOffset + i * ValueLayout.JAVA_SHORT.byteSize();
      int languageId = Short.toUnsignedInt(buffer.get(ValueLayout.JAVA_SHORT, offset));

      var language =
          Language.fromId(languageId)
              .orElseThrow(
                  () ->
                      new UnsupportedOperationException(
                          "Unknown language id %x".formatted(languageId)));
      languages.add(language);
    }

    return languages;
  }
}
