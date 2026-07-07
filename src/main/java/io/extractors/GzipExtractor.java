package io.extractors;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GzipExtractor implements DemoExtractor {

    @Override
    public List<Path> extractDemoFiles(Path sourcePath) throws IOException {
        return List.of();
    }

    private Path getTargetPath(Path source){
        String currentFileName = source.getFileName().toString();
        return source.resolveSibling(currentFileName.substring(0, currentFileName.length()-3));
    }
}
