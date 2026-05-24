package network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class FileDownloader {
    private final HttpClient httpClient;

    public FileDownloader(HttpClient httpClient){
        this.httpClient = httpClient;
    }

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
