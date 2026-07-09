package io.extractors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract base class for demo extraction using the Template Method pattern.
 * Resolves the shared workspace directory and enforces the unmodifiable list
 * contract before delegating the actual decompression logic to its subclasses.
 */
public abstract class AbstractDemoExtractor implements DemoExtractor {

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> This method is marked final to guarantee that extraction
     * occurs directly in the shared workspace, and to enforce that the returned
     * list of file paths is completely unmodifiable.
     * Subclasses must implement the actual extraction logic inside {@link #performExtraction(Path, Path)}.
     * </p>
     */
    @Override
    public final List<Path> extractDemoFiles(Path sourcePath) throws IOException {
        Path targetDir = sourcePath.getParent();

        List<Path> extractedFiles = performExtraction(sourcePath, targetDir);

        return List.copyOf(extractedFiles);
    }

    /**
     * Utility method for multi-file archives (like .zip).
     * Scans a directory and returns a list of all .dem files inside it.
     * Subclasses can call this at the end of their performExtraction method if needed.
     */
    protected List<Path> scanForDemos(Path workspaceDir) throws IOException {
        try (Stream<Path> paths = Files.list(workspaceDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".dem"))
                    .collect(Collectors.toList());
        }
    }

    /**
     * The hook method where subclasses implement their specific decompression logic (e.g., GZIP or ZIP).
     *
     * @param source    The path to the compressed source file.
     * @param targetDir The path to the shared match workspace directory.
     * @return A List containing the paths to the extracted .dem file(s).
     * @throws IOException If an error occurs while reading the archive or writing the uncompressed files.
     */
    protected abstract List<Path> performExtraction(Path source, Path targetDir) throws IOException;
}