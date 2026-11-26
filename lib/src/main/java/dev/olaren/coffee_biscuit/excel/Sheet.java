package dev.olaren.coffee_biscuit.excel;

import dev.olaren.coffee_biscuit.GameData;
import dev.olaren.coffee_biscuit.excel.data_types.PageInfos;
import dev.olaren.coffee_biscuit.sqpack.SqPackPath;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Sheet<T extends SheetItem> implements AutoCloseable {
  private final List<Page<T>> pages;
  private final long baseVariableDataOffset;

  private Sheet(List<Page<T>> pages, long baseVariableDataOffset) {
    this.pages = pages;
    this.baseVariableDataOffset = baseVariableDataOffset;
  }

  public static <T extends SheetItem> Sheet<T> load(
      String name, Language language, SheetHeader header, GameData gameData) throws IOException {
    if (!header.languages().contains(language)) {
      throw new NoSuchFileException(
          "Language %s does not exist for sheet \"%s\"".formatted(language, name));
    }

    String languageCode = language.getCode();

    List<Page<T>> pages = new ArrayList<>(header.pagesInfos().size());
    for (PageInfos pageInfos : header.pagesInfos()) {
      SqPackPath path = resolvePagePath(name, language, pageInfos, languageCode);

      Page<T> page = gameData.readFile(path, Page::fromBytes);
      pages.add(page);
    }

    return new Sheet<>(pages, header.baseVariableDataOffset());
  }

  private static SqPackPath resolvePagePath(
      String name, Language language, PageInfos pageInfos, String languageCode) {
    if (Language.NONE.equals(language)) {
      return SqPackPath.fromString("exd/%s_%s.exd".formatted(name, pageInfos.startId()));
    }

    return SqPackPath.fromString(
        "exd/%s_%s_%s.exd".formatted(name, pageInfos.startId(), languageCode));
  }

  public T getRow(Class<T> clazz, long rowId) {
    return pages.stream()
        .filter(p -> p.containsRow(rowId))
        .map(p -> p.getRow(clazz, rowId, baseVariableDataOffset))
        .findFirst()
        .orElseThrow(
            () -> new NoSuchElementException("Could not fine row with id %s".formatted(rowId)));
  }

  public Stream<T> getRows(Class<T> clazz) {
    return pages.stream().flatMap(p -> p.getRows(clazz, baseVariableDataOffset));
  }

  @Override
  public void close() {
    for (Page<T> page : pages) {
      page.close();
    }
  }
}
