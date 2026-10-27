package dev.olaren.coffee_biscuit.sqpack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GameData {
  private final HashMap<String, Repository> repositories;

  private GameData(HashMap<String, Repository> repositories) {
    this.repositories = repositories;
  }

  public static GameData open(Path root) throws IOException {
    if (!Files.isDirectory(root)) {
      throw new IllegalArgumentException("The provided path is not a directory");
    }

    if (!root.endsWith("sqpack")) {
      throw new IllegalArgumentException("The provided path must point to the sqpack directory");
    }

    var repositories = new HashMap<String, Repository>();
    try (var repoRoots = Files.newDirectoryStream(root, Files::isDirectory)) {
      for (Path repoRoot : repoRoots) {
        var repo = Repository.open(repoRoot);
        repositories.put(repoRoot.getFileName().toString(), repo);
      }
    }

    return new GameData(repositories);
  }
}
