package dev.olaren.coffee_biscuit.sqpack;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.zip.CRC32;

public record SqPackPath(Category category, String repositoryName, long index2Hash, String raw) {
  // Will ffxiv live up to expac 10? Let's find out together bro...
  // This joke will probably be so outdated when I come back here in 6-7 years
  // hehe 67
  private static final String[] EXPAC_REPOSITORY_NAMES = {
    "ex1", "ex2", "ex3", "ex5", "ex6", "ex7", "ex8", "ex9"
  };
  private static final String DEFAULT_REPOSITORY_NAME = "ffxiv";

  public static SqPackPath fromString(String path) {
    if (path.length() >= 260) { // Java's String.length() will never betray me right?
      throw new IllegalArgumentException("sqpack path cannot exceed 269 bytes");
    }

    String[] segments = path.split("/");
    if (segments.length < 2) {
      throw new IllegalArgumentException("Not a valid sqpack path");
    }

    var category =
        Category.fromName(segments[0])
            .orElseThrow(
                () -> new IllegalArgumentException("Unknown category %s".formatted(segments[0])));

    String repositoryName = DEFAULT_REPOSITORY_NAME;
    for (String repoName : EXPAC_REPOSITORY_NAMES) {
      if (repoName.equals(segments[1])) {
        repositoryName = repoName;
        break;
      }
    }

    CRC32 index2Hash = new CRC32();
    byte[] lowerBytes = path.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8);
    index2Hash.update(lowerBytes);
    long computedHash = (int) ~index2Hash.getValue(); // ?????

    return new SqPackPath(category, repositoryName, computedHash, path);
  }
}
