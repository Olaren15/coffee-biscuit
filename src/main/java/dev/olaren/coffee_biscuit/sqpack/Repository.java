package dev.olaren.coffee_biscuit.sqpack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Repository {
  private final HashMap<Category, List<Index2>> indices;

  private Repository(HashMap<Category, List<Index2>> indices) {
    this.indices = indices;
  }

  public static Repository open(Path root) throws IOException {
    var indices = new HashMap<Category, List<Index2>>();

    // TODO: figure out if index files are *really* needed
    try (var index2Paths = Files.newDirectoryStream(root, "*.index2")) {
      for (Path path : index2Paths) {
        var fileName = path.getFileName().toString();
        byte id = Byte.parseByte(fileName.substring(0, 2), 16);
        var category =
            Category.fromId(id)
                .orElseThrow(
                    () ->
                        new UnsupportedOperationException(
                            "Could not find category with id 0x%02X".formatted(id)));
        var index = Index2.loadFromPath(path);

        indices.computeIfAbsent(category, _ -> new ArrayList<>()).add(index);
      }
    }

    return new Repository(indices);
  }
}
