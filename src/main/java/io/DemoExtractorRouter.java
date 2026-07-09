package io;

import io.extractors.DemoExtractor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acts as a registry and router for demo file extraction.
 * Dynamically routes downloaded compressed files to the appropriate extractor
 * (e.g., GZIP, ZIP) based on their specific file extension.
 */
public class DemoExtractorRouter {

    private final Map<String, DemoExtractor> extractors = new HashMap<>();

    /**
     * Registers a new extraction tool to the routing table.
     * Uses a fluent interface (Builder pattern) to allow for method chaining.
     *
     * @param extractor The specific DemoExtractor implementation to register.
     * @return This DemoExtractorRouter instance, for chaining.
     */
    public DemoExtractorRouter registerExtractor(DemoExtractor extractor) {
        extractors.put(extractor.getSupportedFileExtension(), extractor);
        return this;
    }

    /**
     * Identifies the file type of the downloaded archives and delegates the
     * unzipping process to the corresponding registered extractors.
     *
     * @param sourcePaths A list of paths to the downloaded compressed files (e.g., .gz or .zip).
     * @return An unmodifiable List containing the paths to all successfully extracted .dem files.
     * @throws IOException If a file system error occurs during the extraction process.
     * @throws RuntimeException If no compatible extractor is found for a given file type.
     */
    public List<Path> extractAll(List<Path> sourcePaths) throws IOException {
        List<Path> allExtractedDemos = new ArrayList<>();

        for (Path sourcePath : sourcePaths) {
            DemoExtractor extractor = null;
            String fileName = sourcePath.getFileName().toString();

            for (String e : extractors.keySet()) {
                if (fileName.endsWith(e)) {
                    extractor = extractors.get(e);
                    break;
                }
            }

            if (extractor != null) {
                allExtractedDemos.addAll(extractor.extractDemoFiles(sourcePath));
            } else {
                throw new RuntimeException("File type not supported: " + sourcePath.getFileName());
            }
        }

        return List.copyOf(allExtractedDemos);
    }
}
