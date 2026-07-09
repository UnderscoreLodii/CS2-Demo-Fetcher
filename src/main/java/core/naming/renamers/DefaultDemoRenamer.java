package core.naming.renamers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The fallback naming strategy. Used when a demo is downloaded from an
 * unrecognized or unsupported platform.
 */
public final class DefaultDemoRenamer extends AbstractDemoRenamer {

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> Generates a safe, unique fallback name using the current system timestamp.
     * </p>
     */
    @Override
    protected String generateDefaultName(String matchpageUrl) {
        return "Demo_" + System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> Provides a best-effort alphabetical sort based on the raw filenames.
     * </p>
     */
    @Override
    protected List<Path> sortDemoFiles(List<Path> rawFiles, String matchpageUrl) {
        List<Path> sortedFiles = new ArrayList<>(rawFiles);
        sortedFiles.sort(Comparator.comparing(Path::getFileName));

        return sortedFiles;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> Returns null because this strategy is manually injected as a fallback,
     * not dynamically routed via a specific domain name.
     * </p>
     */
    @Override
    public String getSupportedSiteHost() {
        return null;
    }
}
