package dev.olaren.coffee_biscuit;

import dev.olaren.coffee_biscuit.excel.*;
import dev.olaren.coffee_biscuit.excel.reflection.SheetName;
import dev.olaren.coffee_biscuit.sqpack.SqPackPath;
import java.io.IOException;
import java.util.*;

public class Excel {
  private static final SqPackPath EXCEL_LIST_FILE_PATH = SqPackPath.fromString("exd/root.exl");
  private final GameData gameData;
  private final ExcelList excelList;
  private final Map<String, SheetHeader> headers;

  private Excel(GameData gameData, ExcelList excelList, Map<String, SheetHeader> headers) {
    this.gameData = gameData;
    this.excelList = excelList;
    this.headers = headers;
  }

  public static Excel load(GameData gameData) throws IOException {
    ExcelList list = gameData.readFile(EXCEL_LIST_FILE_PATH, ExcelList::fromBytes);

    HashMap<String, SheetHeader> headers = new HashMap<>();
    for (Map.Entry<String, ExcelList.SheetProps> entry : list.getSheetsProps()) {
      SheetHeader header = gameData.readFile(entry.getValue().headerPath(), SheetHeader::fromBytes);

      headers.put(entry.getKey(), header);
    }

    return new Excel(gameData, list, Collections.synchronizedMap(headers));
  }

  public <T extends SheetItem> Sheet<T> openSheet(Class<T> clazz, Language language)
      throws IOException {
    String sheetName = clazz.getAnnotation(SheetName.class).value().toLowerCase(Locale.ROOT);
    ExcelList.SheetProps props =
        excelList
            .getSheetProps(sheetName)
            .orElseThrow(
                () ->
                    new UnsupportedOperationException(
                        "Class %s does not represent an existing sheet"
                            .formatted(clazz.getName())));

    SheetHeader header = headers.get(sheetName);
    if (header == null) {
      header = gameData.readFile(props.headerPath(), SheetHeader::fromBytes);
    }

    return Sheet.load(sheetName, language, header, gameData);
  }
}
