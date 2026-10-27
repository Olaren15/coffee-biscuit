package dev.olaren.coffee_biscuit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class CoffeeBiscuitTest {
  @Test
  void validPath_loadsGameData() throws IOException {
    var data = CoffeeBiscuit.openGameFiles(Path.of("/home/olaren/.xlcore/ffxiv/game/sqpack"));

    assertNotNull(data);
  }
}
