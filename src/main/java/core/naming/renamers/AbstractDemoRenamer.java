package core.naming.renamers;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract skeletal implementation of the {@link DemoRenamer} interface that
 * employs the Template Method Pattern to standardize the demo naming process.
 * <p>
 * This class orchestrates the overall flow: checking for custom user inputs,
 * resolving base names, determining single vs. multi-map contexts, and appending
 * chronological map suffixes. It delegates site-specific variations (like parsing
 * default match names and sorting files chronologically) to its concrete subclasses.
 * </p>
 * This class guarantees that all generated maps are defensively copied and completely
 * unmodifiable before being returned to the pipeline.
 */
public abstract class AbstractDemoRenamer implements DemoRenamer {

    /**
     * Executes the structural skeleton of the naming algorithm.
     * Handles single vs. multi-map branches, automatically appends suffixes (`_map1`, `_map2`),
     * and shields the output map against downstream modification.
     * <p>
     * Note: This method is marked final to guarantee the integrity of the naming sequence
     * and enforce the read-only Map contract. Subclasses must customize behavior using
     * the {@link #generateDefaultName(String)} and {@link #sortDemoFiles(List, String)} hooks.
     * </p>
     *
     * @param demoFilePaths A list of paths pointing to the raw, unzipped temporary demo files.
     * @param matchpageUrl  The original URL where the demo was downloaded from.
     * @param customName    An optional user-provided string to use as the base name (may be null or empty).
     * @return An unmodifiable Map mapping the file {@link Path} to its newly formatted name {@link String}.
     */
    @Override
    public final Map<Path, String> generateNameMap(List<Path> demoFilePaths, String matchpageUrl, String customName) {
        Map<Path, String> nameMap = new HashMap<>();

        boolean useDefaultName = customName == null || customName.isBlank();
        boolean multipleFiles = demoFilePaths.size() > 1;

        String finalName = useDefaultName ? generateDefaultName(matchpageUrl) : customName;

        if(multipleFiles) {
            List<Path> orderedDemoFilePaths = sortDemoFiles(demoFilePaths, matchpageUrl);

            for(int i = 0 ; i < orderedDemoFilePaths.size(); i++) {
                Path currentDemoFilePath = orderedDemoFilePaths.get(i);
                String mapFinalName = finalName + "_map" + (i+1);

                nameMap.put(currentDemoFilePath, mapFinalName);
            }
        }
        else nameMap.put(demoFilePaths.getFirst(), finalName);

        return Map.copyOf(nameMap);
    }

    /**
     * Concrete subclasses must implement this to parse the given match page URL
     * or metadata and generate a fallback name when the user leaves the custom name empty.
     *
     * @param matchpageUrl The original match page link.
     * @return A generated base name string (e.g., "Navi_vs_FaZe" or a timestamp fallback).
     */
    protected abstract String generateDefaultName(String matchpageUrl);

    /**
     * Concrete subclasses must implement this to order the raw unzipped demo files chronologically.
     * This protects the pipeline from chaotic OS-level unzipping sequences and ensures Map 1
     * is genuinely assigned the `_map1` suffix.
     *
     * @param rawFiles     The unordered batch of temporary files extracted from the archive.
     * @param matchpageUrl The original match link, which may assist in calculating chronological sequence.
     * @return A newly sorted List of Paths arranged in actual match-play order.
     */
    protected abstract List<Path> sortDemoFiles(List<Path> rawFiles, String matchpageUrl);
}
