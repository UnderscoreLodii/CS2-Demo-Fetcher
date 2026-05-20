package network.providers;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaceitProvider implements DemoProvider {

    private static final Pattern correctPathPattern = Pattern.compile("^/(?:[a-zA-Z]{2}/)?cs2/room/(\\d+-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})(?:/[a-zA-Z0-9_-]+)?/?$");
    private final HttpClient httpClient;


    public FaceitProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getProviderSiteHost() {
        return "www.faceit.com";
    }

    @Override
    public String getDownloadLink(URI uri) {

        String matchId = extractMatchId(uri);

        String jsonResponse = fetchMatchData(matchId);

        return extractDemoUrlFromJson(jsonResponse);
    }

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

        } catch (IOException | InterruptedException e){
            throw new RuntimeException("Network error: Could not connect to Faceit.", e);
        }
    }

    private String extractDemoUrlFromJson(String json) {
        return "";
    }

    //temporary solution
    private String getApiKey(){
        return Dotenv.load().get("FACEIT_API_KEY");
    }

}
