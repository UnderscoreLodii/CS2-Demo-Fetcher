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
     * Decompresses a demo archive into its current directory (the match workspace).
     *
     * @param sourcePath  The path to the downloaded compressed file (e.g., .gz or .zip).
     * @return An unmodifiable List containing the paths to all successfully extracted .dem files.
     * @throws IOException If a file system error occurs or the archive is corrupted.
     */
    List<Path> extractDemoFiles(Path sourcePath) throws IOException;

    /**
     * Retrieves the specific file extension that this extractor is designed to handle.
     * This allows the application to dynamically route downloaded files to the correct unzipper.
     *
     * @return The supported file extension as a String (e.g., ".gz" or ".zip").
     */
    String getSupportedFileExtension();
}
