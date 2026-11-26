package dev.olaren.coffee_biscuit.examples;

import dev.olaren.coffee_biscuit.CoffeeBiscuit;
import dev.olaren.coffee_biscuit.excel.Language;
import dev.olaren.coffee_biscuit.excel.Sheet;
import dev.olaren.coffee_biscuit.excel.sheet.Orchestrion;
import java.io.IOException;
import java.nio.file.Path;

public class ExcelSheetRead {
  static void main() throws IOException {
    var data = CoffeeBiscuit.openGameData(Path.of("/home/olaren/.xlcore/ffxiv/game/sqpack"));

    var excel = CoffeeBiscuit.openExcelSheets(data);
    try (Sheet<Orchestrion> sheet = excel.openSheet(Orchestrion.class, Language.ENGLISH)) {
      sheet
          .getRows(Orchestrion.class)
          .filter(o -> o.getName().contains("a"))
          .forEach(o -> IO.println("%d: %s".formatted(o.getId(), o.getName())));
    }
  }
}
