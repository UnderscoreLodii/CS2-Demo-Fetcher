package network;

import core.models.MatchContext;
import network.providers.MatchContextProvider;

import java.net.URI;
import java.util.HashMap;

/**
 * Routes match room URLs to the appropriate platform-specific {@link MatchContextProvider}.
 * Acts as the central registry for all supported network fetchers, delegating URL parsing
 * and metadata extraction to the correct platform implementation.
 */
public class MatchContextProviderRouter {
    private final HashMap<String, MatchContextProvider> providers = new HashMap<>();

    /**
     * Registers a new MatchContextProvider to handle its specific host domain.
     *
     * @param provider The platform-specific provider to register (e.g., Faceit, HLTV).
     * @return The current MatchContextProviderRouter instance to allow method chaining.
     */
    public MatchContextProviderRouter registerProvider(MatchContextProvider provider) {
        providers.put(provider.getProviderSiteHost(), provider);
        return this;
    }

    /**
     * Resolves the correct provider for the given URL and extracts the match metadata.
     *
     * @param matchPageUrl The raw match room URL string provided by the user.
     * @return A richly populated {@link MatchContext} record containing the metadata and download links.
     * @throws IllegalArgumentException If the URL is malformed or the host domain is currently unsupported.
     */
    public MatchContext getMatchContext(String matchPageUrl) throws IllegalArgumentException {
        URI uri = URI.create(matchPageUrl);
        MatchContextProvider provider = providers.get(uri.getHost());

        if (provider != null) {
            return provider.getMatchContext(uri);
        } else {
            throw new IllegalArgumentException("Unsupported host: " + uri.getHost());
        }
    }
}
