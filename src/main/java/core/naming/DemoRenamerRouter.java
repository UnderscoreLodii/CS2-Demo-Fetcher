package core.naming;

import core.models.MatchContext;
import core.naming.renamers.DemoRenamer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acts as a registry and router for platform-specific demo naming strategies.
 * Dynamically routes a batch of extracted demo files to the appropriate renaming tool
 * based on the host domain of the original match URL.
 */
public class DemoRenamerRouter {

    private final Map<String, DemoRenamer> renamers = new HashMap<>();

    private final DemoRenamer fallbackRenamer;

    /**
     * Constructs the router with a mandatory fallback naming strategy.
     * @param fallbackRenamer The default naming strategy to use if a URL's domain is not explicitly registered.
     */
    public DemoRenamerRouter(DemoRenamer fallbackRenamer) {
        this.fallbackRenamer = fallbackRenamer;
    }

    /**
     * Registers a specific naming strategy to the routing table.
     * Uses a fluent interface (Builder pattern) to allow for method chaining during initialization.
     *
     * @param renamer The specific DemoRenamer implementation to register.
     * @return This DemoRenamerRouter instance, for chaining.
     */
    public DemoRenamerRouter registerRenamer(DemoRenamer renamer) {
        this.renamers.put(renamer.getSupportedSiteHost(), renamer);
        return this;
    }

    /**
     * Identifies the platform domain from the provided context and delegates the
     * file renaming logic to the corresponding registered strategy, or the fallback if unknown.
     *
     * @param demoFilePaths The list of raw, unzipped temporary demo files.
     * @param matchContext  The context containing match metadata and platform details.
     * @param customName    An optional user-provided name (can be null or blank).
     * @return A Map linking each original temporary file Path to its new, formatted filename String.
     */
    public Map<Path, String> generateNameMap(List<Path> demoFilePaths, MatchContext matchContext, String customName) {
        String siteHost = matchContext.siteHost();

        DemoRenamer renamer = renamers.getOrDefault(siteHost,  fallbackRenamer);

        return renamer.generateNameMap(demoFilePaths, matchContext, customName);
    }
}
