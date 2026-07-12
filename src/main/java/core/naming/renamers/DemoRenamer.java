package core.naming.renamers;

import core.models.MatchContext;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Defines the contract for platform-specific demo naming strategies.
 * Any class implementing this interface handles the business logic of
 * translating raw, temporary demo files into beautifully formatted filenames
 * based on the platform's specific layout (e.g., Faceit, HLTV, or a fallback).
 */
public interface DemoRenamer {

    /**
     * Generates an immutable mapping between the temporary extracted file paths and
     * their finalized, clean filenames.
     *
     * @param demoFilePaths A list of paths pointing to the raw, unzipped temporary demo files.
     * @param matchContext  The context containing match metadata and platform details.
     * @param customName    An optional user-provided string to use as the base name (may be null or empty).
     * @return An unmodifiable Map linking each original temporary file Path to its new, formatted filename String.
     * @throws UnsupportedOperationException if any attempt is made to mutate the returned map down the pipeline.
     */
    Map<Path, String> generateNameMap(List<Path> demoFilePaths, MatchContext matchContext, String customName);

    /**
     * Retrieves the specific web domain that this renaming strategy is designed to handle.
     * This acts as the exact routing key for the DemoRenamerRouter registry.
     *
     * @return The supported site host domain as a String (e.g., "www.faceit.com", "hltv.org"),
     * or a unique identifier if used as a fallback strategy.
     */
    String getSupportedSiteHost();
}