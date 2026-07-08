package io.extractors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Extracts single-file .gz compressed Counter-Strike demos.
 */
public class GzipDemoExtractor extends AbstractDemoExtractor {

    @Override
    protected List<Path> performExtraction(Path source, Path targetDir) {

        String cleanFileName = source.getFileName().toString().replace(".gz", "");
        Path targetPath = targetDir.resolve(cleanFileName);

        try(
            FileInputStream fis = new FileInputStream(source.toFile());
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream os = new  FileOutputStream(targetPath.toFile());
        ){

            gis.transferTo(os);

        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress GZIP file: " + source.getFileName(), e);
        }

        return List.of(targetPath);
    }

    @Override
    public String getSupportedFileExtension() {
        return ".gz";
    }
}
