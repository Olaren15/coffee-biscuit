package dev.olaren.coffee_biscuit.sqpack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class Repository {
  private final MultiValuedMap<Category, Index2> indicesByCategory;

  private Repository(MultiValuedMap<Category, Index2> indicesByCategory) {
    this.indicesByCategory = indicesByCategory;
  }

  public static Repository open(Path root) throws IOException {
    var indices = new ArrayListValuedHashMap<Category, Index2>();

    // only using index2 files is working fine *so far*
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

        indices.put(category, index);
      }
    }

    return new Repository(indices);
  }

  public Optional<SqPackFileLocation> locateFile(SqPackPath path) {
    Collection<Index2> indices = indicesByCategory.get(path.category());
    if (indices == null) {
      return Optional.empty();
    }

    return indices.stream()
        .map(index -> index.getFileLocation(path))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }
}
