package io.extractors;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GzipExtractor extends AbstractDemoExtractor {

    @Override
    protected List<Path> performExtraction(Path source, Path targetDir) {

        return List.of();
    }
}
