package dev.olaren.coffee_biscuit.examples;

import dev.olaren.coffee_biscuit.CoffeeBiscuit;
import dev.olaren.coffee_biscuit.sqpack.RawSqPackFile;
import dev.olaren.coffee_biscuit.sqpack.SqPackPath;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class RawFileRead {
  static void main() throws IOException {
    var data = CoffeeBiscuit.openGameData(Path.of("/home/olaren/.xlcore/ffxiv/game/sqpack"));

    var path = SqPackPath.fromString("exd/root.exl");
    try (RawSqPackFile rootExl = data.readFile(path, RawSqPackFile::fromBytes)) {
      String text = StandardCharsets.UTF_8.decode(rootExl.data().asByteBuffer()).toString();

      System.out.print(text);
    }
  }
}
