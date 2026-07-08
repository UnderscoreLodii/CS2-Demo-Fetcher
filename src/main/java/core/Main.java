package core;

import core.naming.DemoRenamer;
import io.DemoExtractorRouter;
import io.DemoFileManager;
import io.extractors.GzipDemoExtractor;
import network.DemoURLProviderRouter;
import network.FileDownloader;
import network.providers.FaceitURLProvider;

import java.net.http.HttpClient;

public class Main {
    static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        FileDownloader fileDownloader = new FileDownloader(httpClient);
        DemoURLProviderRouter demoURLProviderRouter = new DemoURLProviderRouter()
                .registerProvider(new FaceitURLProvider(httpClient));

        DemoFileManager demoFileManager = new DemoFileManager();
        DemoExtractorRouter demoExtractorRouter = new DemoExtractorRouter()
                .registerExtractor(new GzipDemoExtractor());

        DemoRenamer demoRenamer = new DemoRenamer();

        MatchProcessor matchProcessor = new MatchProcessor(demoURLProviderRouter, fileDownloader, demoExtractorRouter, demoRenamer, demoFileManager);
    }
}
