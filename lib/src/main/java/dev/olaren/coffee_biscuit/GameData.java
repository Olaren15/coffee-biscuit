package dev.olaren.coffee_biscuit;

import dev.olaren.coffee_biscuit.sqpack.*;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;

public class GameData {
  private final Map<String, Repository> repositoriesByName;
  private final Map<Path, MemorySegment> mappedFilesCache;

  private GameData(
      Map<String, Repository> repositoriesByName, Map<Path, MemorySegment> mappedFilesCache) {
    this.repositoriesByName = repositoriesByName;
    this.mappedFilesCache = mappedFilesCache;
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

    // Allow gc to drop mapped files if memory is needed and there are no other references to it
    var mappedFilesCache =
        new ReferenceMap<Path, MemorySegment>(
            AbstractReferenceMap.ReferenceStrength.HARD,
            AbstractReferenceMap.ReferenceStrength.SOFT);

    return new GameData(
        // Read only from now
        Collections.unmodifiableMap(repositories),
        // Synchronize accesses across threads
        Collections.synchronizedMap(mappedFilesCache));
  }

  public <T> T readFile(SqPackPath path, SqPackFileParser<T> parser) throws IOException {
    SqPackFileLocation location =
        Optional.of(repositoriesByName.get(path.repositoryName()))
            .flatMap(repo -> repo.locateFile(path))
            .orElseThrow(() -> new NoSuchFileException(path.raw()));

    MemorySegment mapped = mapFile(location.filePath());

    return SqPackFileReader.read(mapped, location.byteOffset(), parser);
  }

  private MemorySegment mapFile(Path path) throws IOException {
    if (mappedFilesCache.containsKey(path)) {
      return mappedFilesCache.get(path);
    }

    try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
      // auto arena stays valid as long as all of its segments are
      return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size(), Arena.ofAuto());
    }
  }
}
