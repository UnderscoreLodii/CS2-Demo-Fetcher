package network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

/**
 * Utility class for downloading files directly to the file system using a shared HttpClient.
 */
public class FileDownloader {
    private final HttpClient httpClient;


    /**
     * Constructs a FileDownloader with an injected HttpClient.
     *
     * @param httpClient The shared HTTP client for executing download requests.
     */
    public FileDownloader(HttpClient httpClient){
        this.httpClient = httpClient;
    }

    /**
     * Executes an HTTP GET request to download a file from a URL directly to a specified path.
     *
     * @param targetPath The local file path where the downloaded file should be saved.
     * @param url The direct download URL.
     * @return The path to the successfully downloaded file.
     * @throws RuntimeException If the HTTP status is not 200, the thread is interrupted, or a network error occurs.
     */
    public Path downloadFile(Path targetPath,String url){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(targetPath));
            if(response.statusCode() == 200) {
                return response.body();
            }
            else{
                throw new RuntimeException("Download failed. Server returned HTTP status " + response.statusCode() + " for URL: " + url);
            }

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Download was cancelled before it could finish.", e);

        } catch (IOException e){
            throw new RuntimeException("Network error: Could not reach the download server.", e);
        }
    }
}
