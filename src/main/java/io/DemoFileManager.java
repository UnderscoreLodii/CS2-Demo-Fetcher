package io;

import java.io.IOException;
import java.nio.file.*;

public class DemoFileManager {

    public void manageFile(Path source, Path targetDir, String baseName) throws IOException {
        String extension = ".dem";
        Path target = targetDir.resolve(baseName + extension);

        int counter = 0;
        while (Files.exists(target)) {
            counter++;
            target = targetDir.resolve(baseName + "(" + counter + ")" + extension);
        }

        Files.move(source, target);
    }

    public void manageFile(Path source, Path targetDir) throws IOException {
        //add default name to config and settings later
        manageFile(source, targetDir, "FaceitDemo");
    }
}
