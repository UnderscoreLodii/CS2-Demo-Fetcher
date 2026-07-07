package network;

import network.providers.DemoProvider;

import java.net.URI;
import java.util.HashMap;

/**
 * Routes matchroom URLs to the appropriate platform-specific DemoProvider.
 */
public class DemoRouter {
    private final HashMap<String, DemoProvider> providers = new HashMap<>();

    /**
     * Registers a new DemoProvider to handle its specific host.
     *
     * @param provider The provider to register.
     * @return The current DemoRouter instance to allow method chaining.
     */
    public DemoRouter registerProvider(DemoProvider provider){
        providers.put(provider.getProviderSiteHost(), provider);
        return this;
    }

    /**
     * Resolves the correct provider for the given URL and retrieves the download link.
     *
     * @param matchPageUrl The raw matchroom URL string.
     * @return The direct download link extracted by the corresponding provider.
     * @throws IllegalArgumentException If the URL host is unsupported or the URL is malformed.
     */
    public String getDownloadLink(String matchPageUrl) throws IllegalArgumentException {
        URI uri = URI.create(matchPageUrl);
        DemoProvider provider = providers.get(uri.getHost());
        if (provider != null) {
            return provider.getDownloadLink(uri);
        }
        else throw new IllegalArgumentException("Unsupported host " + uri.getHost());
    }
}
