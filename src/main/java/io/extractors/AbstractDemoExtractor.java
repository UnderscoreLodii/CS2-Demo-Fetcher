package io.extractors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract base class for demo extraction using the Template Method pattern.
 * Automatically handles the creation of a secure, isolated workspace directory
 * before delegating the actual decompression logic to its subclasses.
 */
public abstract class AbstractDemoExtractor implements DemoExtractor {

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> This base class automatically creates an isolated
     * workspace folder next to the source file before extracting.
     */
    @Override
    public List<Path> extractDemoFiles(Path sourcePath) throws IOException {
        Path targetDir = createExtractionWorkspace(sourcePath);

        return performExtraction(sourcePath, targetDir);
    }

    /*
     * Generates an isolated workspace folder named after the source file (e.g., "match123_extracted").
     */
    private Path createExtractionWorkspace(Path source) throws IOException {
        String name = source.getFileName().toString()
                .replace(".dem.gz", "")
                .replace(".zip", "") + "_extracted";

        Path targetDir = source.resolveSibling(name);
        Files.createDirectories(targetDir);

        return targetDir;
    }

    /**
     * Utility method for multi-file archives (like .zip).
     * Scans a directory and returns a list of all .dem files inside it.
     * Subclasses can call this at the end of their performExtraction method if needed.
     */
    protected List<Path> scanForDemos(Path workspaceDir) throws IOException{
        try(Stream<Path> paths = Files.list(workspaceDir)){
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path ->  path.getFileName().toString().endsWith(".gz"))
                    .collect(Collectors.toList());
        }
    }

    /**
     * The hook method where subclasses implement their specific decompression logic (e.g., GZIP or ZIP).
     * Subclasses can safely assume the target directory already exists and is empty.
     *
     * @param source    The path to the compressed source file.
     * @param targetDir The path to the newly created, empty workspace directory.
     * @return A List containing the paths to the extracted .dem file(s).
     * @throws IOException If an error occurs while reading the archive or writing the uncompressed files.
     */
    protected abstract List<Path> performExtraction(Path source, Path targetDir) throws IOException;
}
