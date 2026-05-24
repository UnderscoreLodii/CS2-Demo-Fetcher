package core;

import network.DemoRouter;
import network.FileDownloader;
import network.providers.FaceitProvider;

import java.net.http.HttpClient;

public class Main {
    static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        FileDownloader fileDownloader = new FileDownloader(httpClient);
        DemoRouter demoRouter = new DemoRouter()
                .registerProvider(new FaceitProvider(httpClient));
    }
}
