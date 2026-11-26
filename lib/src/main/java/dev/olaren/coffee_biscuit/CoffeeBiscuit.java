package dev.olaren.coffee_biscuit;

import java.io.IOException;
import java.nio.file.Path;

public class CoffeeBiscuit {
  public static GameData openGameData(Path root) throws IOException {
    return GameData.open(root);
  }

  public static Excel openExcelSheets(GameData gameData) throws IOException {
    return Excel.load(gameData);
  }
}
