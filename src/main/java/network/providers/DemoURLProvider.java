package network.providers;

import java.net.URI;

/**
 * Defines the contract for platform-specific demo fetchers.
 * Each provider is responsible for handling URLs from its designated host.
 */
public interface DemoURLProvider {

    /**
     * Retrieves the hostname that this provider is responsible for resolving.
     *
     * @return The hostname string (e.g., "www.faceit.com").
     */
    String getProviderSiteHost();

    /**
     * Extracts and retrieves the direct download link for a given match URI.
     *
     * @param uri The matchroom URI.
     * @return The direct download link.
     */
    String getDownloadLink(URI uri);
}
