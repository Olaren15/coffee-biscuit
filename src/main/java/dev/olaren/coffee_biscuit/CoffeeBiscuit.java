package dev.olaren.coffee_biscuit;

import dev.olaren.coffee_biscuit.sqpack.GameData;
import java.io.IOException;
import java.nio.file.Path;

public class CoffeeBiscuit {
  private final GameData gameData;

  private CoffeeBiscuit(GameData gameData) {
    this.gameData = gameData;
  }

  public static CoffeeBiscuit openGameFiles(Path root) throws IOException {
    var gameData = GameData.open(root);

    return new CoffeeBiscuit(gameData);
  }
}
