package io;

import java.io.IOException;
import java.nio.file.*;

/**
 * Utility class responsible for managing and organizing unzipped Counter-Strike demo files.
 * Handles safe file relocation and automatically resolves file naming collisions.
 */
public class DemoFileManager {

    /**
     * Moves a demo file to a target directory with a specified base name, safely handling naming collisions.
     * If a file with the target name already exists, it appends a sequential number to the filename
     * (e.g., "FaceitDemo (1).dem") to prevent overwriting.
     *
     * @param source    The current path of the file to be moved.
     * @param targetDir The directory where the file should be permanently saved.
     * @param baseName  The desired name for the file, excluding the ".dem" extension.
     * @throws IOException If a file system error occurs during the move operation.
     */
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
}
