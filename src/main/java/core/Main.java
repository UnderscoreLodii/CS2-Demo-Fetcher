package core;

import network.DemoRouter;
import network.providers.FaceitProvider;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        DemoRouter demoRouter = new DemoRouter()
                .registerProvider(new FaceitProvider(httpClient));
    }
}
