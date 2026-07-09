package network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
     * Executes HTTP GET requests to download a list of files directly into a designated workspace directory.
     *
     * @param workspaceDir The local directory path where the downloaded files should be saved.
     * @param urls         A list of direct download URLs for the match demos.
     * @return An unmodifiable list of paths to the successfully downloaded files.
     * @throws RuntimeException If a download fails, the thread is interrupted, or a network error occurs.
     */
    public List<Path> downloadFiles(Path workspaceDir, List<String> urls){
        List<Path> downloadedFiles = new ArrayList<>();

        try {
            for(String url : urls){
                String fileName = url.substring(url.lastIndexOf('/') + 1);

                Path targetPath = workspaceDir.resolve(fileName);

                downloadedFiles.add(downloadFile(targetPath, url));
            }
            return List.copyOf(downloadedFiles);

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Download was cancelled before it could finish.", e);

        } catch (IOException e){
            throw new RuntimeException("Network error: Could not reach the download server.", e);
        }
    }

    // Private helper method to handle the actual HTTP transaction
    private Path downloadFile(Path targetPath, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(targetPath));
        if(response.statusCode() == 200) {
            return response.body();
        }
        else{
            throw new RuntimeException("Download failed. Server returned HTTP status " + response.statusCode() + " for URL: " + url);
        }
    }
}
