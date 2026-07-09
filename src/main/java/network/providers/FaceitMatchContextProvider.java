package network.providers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.MatchContext;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of MatchContextProvider for fetching Counter-Strike 2 match demos from the Faceit API.
 */
public class FaceitMatchContextProvider implements MatchContextProvider {

    private static final Pattern correctPathPattern = Pattern.compile("^/(?:[a-zA-Z]{2}/)?cs2/room/(\\d+-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})(?:/[a-zA-Z0-9_-]+)?/?$");
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper =  new ObjectMapper();

    /**
     * Constructs a FaceitMatchContextProvider with an injected HttpClient instance.
     *
     * @param httpClient The shared HTTP client for executing network requests.
     */
    public FaceitMatchContextProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getProviderSiteHost() {
        return "www.faceit.com";
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Implementation Note:</b> For Faceit, this involves extracting the match ID
     * via regex and querying the v4 API to return a .gz file link.
     */
    @Override
    public MatchContext getMatchContext(URI uri) {

        String matchId = extractMatchId(uri);

        String jsonResponse = fetchMatchData(matchId);

        return buildMatchContextFromJson(jsonResponse);
    }

    /*
     * Validates the provided URI and extracts the Faceit match ID using regex.
     */
    private String extractMatchId(URI uri) {
        String path = uri.getPath();
        if (path == null) {
            throw new IllegalArgumentException("Invalid URL: No path found in the link.");
        }

        Matcher matcher = correctPathPattern.matcher(path);

        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("Invalid Faceit Matchroom URL.");
        }
    }

    /*
     * Executes a GET request to the Faceit Match API and returns the JSON payload.
     * Handles specific HTTP status codes for authorization, rate limiting, and missing resources.
     */
    private String fetchMatchData(String matchId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://open.faceit.com/data/v4/matches/" + matchId))
                .header("Authorization", "Bearer " + getApiKey())
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) return response.body();
            else if (response.statusCode() == 401) throw new IllegalStateException("Faceit API Key is invalid.");
            else if (response.statusCode() == 403) throw new IllegalStateException("Access Denied: Your API key doesn't have permission to view this match.");
            else if (response.statusCode() == 404) throw new IllegalArgumentException("Match not found.");
            else if (response.statusCode() == 429) throw new IllegalStateException("Rate limit exceeded. Slow down and try again later.");
            else if (response.statusCode() == 503 || response.statusCode() == 500) throw new RuntimeException("Faceit's servers are currently down or experiencing issues.");
            else throw new RuntimeException("Faceit API failed with unexpected HTTP status: " + response.statusCode());

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Network error: Could not connect to Faceit.", e);
        } catch (IOException e){
            throw new RuntimeException("Network error: Could not connect to Faceit.", e);
        }
    }

    /*
     * Parses the Faceit API JSON response to locate the demo URL array.
     */
    private MatchContext buildMatchContextFromJson(String json) {
        try {
            String url = objectMapper.readTree(json)
                    .path("demo_url")
                    .path(0)
                    .asText();

            if (url.isBlank()) throw new IllegalArgumentException("Demo not available yet. The match might be live, cancelled, or still processing.");

            return url;

        }  catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to read Faceit JSON response.", e);
        }
    }

    /*
     * Retrieves the authorization key needed for the Faceit API.
     * TODO: Replace Dotenv load with centralized configuration manager.
     */
    private String getApiKey(){
        return Dotenv.load().get("FACEIT_API_KEY");
    }

}
