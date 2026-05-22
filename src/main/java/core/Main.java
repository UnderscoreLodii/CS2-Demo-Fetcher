package core;

import network.DemoRouter;
import network.FileDownloader;
import network.NetworkService;
import network.providers.FaceitProvider;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        FileDownloader fileDownloader = new FileDownloader();
        DemoRouter demoRouter = new DemoRouter()
                .registerProvider(new FaceitProvider(httpClient));

        NetworkService networkService = new NetworkService(demoRouter, fileDownloader);
    }
}
