package network.providers;

import core.models.MatchContext;

import java.net.URI;

/**
 * Defines the contract for platform-specific network fetchers.
 * Each provider is responsible for intercepting a match URL for its designated platform,
 * executing the necessary HTTP requests (API or HTML scraping), and packaging the
 * extracted metadata into a unified Data Transfer Object (DTO).
 */
public interface MatchContextProvider {

    /**
     * Retrieves the specific web domain that this provider is designed to handle.
     * This acts as the routing key for the network registry/router.
     *
     * @return The supported site host as a String (e.g., "www.faceit.com" or "hltv.org").
     */
    String getProviderSiteHost();

    /**
     * Executes the network request(s) required to fetch match data and parses the response.
     * It extracts essential metadata (like team names, competition names, and direct download URLs)
     * and bundles them into an immutable record to be passed down the application pipeline.
     *
     * @param uri The original match room URI provided by the user.
     * @return A richly populated {@link MatchContext} record containing the download links and metadata.
     */
    MatchContext getMatchContext(URI uri);
}
