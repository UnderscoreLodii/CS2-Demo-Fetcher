package io.extractors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for extracting compressed Counter-Strike demo files.
 * Supports both single-file compression (like .gz) and multi-file archives (like .zip).
 */
public interface DemoExtractor {

    /**
     * Decompresses a demo archive and writes the raw .dem file(s) to the target directory.
     *
     * @param sourcePath  The path to the downloaded compressed file (e.g., .gz or .zip).
     * @return A List containing the paths to all successfully extracted .dem files.
     * @throws IOException If a file system error occurs or the archive is corrupted.
     */
    List<Path> extractDemoFiles(Path sourcePath) throws IOException;

}
