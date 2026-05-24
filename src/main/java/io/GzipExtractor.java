package io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GzipExtractor {

    public Path extractDemoFile(Path source) throws IOException {
        Path targetPath = getTargetPath(source);

        try(InputStream inputStream = Files.newInputStream(source)){

        }
        catch (IOException){

        }
    }

    public Path getTargetPath(Path source){
        String currentFileName = source.getFileName().toString();
        return source.resolveSibling(currentFileName.substring(0, currentFileName.length()-3));
    }
}
