package core;

import core.naming.DemoRenamerRouter;
import core.naming.renamers.DefaultDemoRenamer;
import core.naming.renamers.FaceitDemoRenamer;
import io.DemoExtractorRouter;
import io.DemoFileManager;
import io.extractors.GzipDemoExtractor;
import network.MatchContextProviderRouter;
import network.FileDownloader;
import network.providers.FaceitMatchContextProvider;

import java.net.http.HttpClient;

public class Main {
    static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        FileDownloader fileDownloader = new FileDownloader(httpClient);
        MatchContextProviderRouter matchContextProviderRouter = new MatchContextProviderRouter()
                .registerProvider(new FaceitMatchContextProvider(httpClient));

        DemoFileManager demoFileManager = new DemoFileManager();
        DemoExtractorRouter demoExtractorRouter = new DemoExtractorRouter()
                .registerExtractor(new GzipDemoExtractor());

        DemoRenamerRouter demoRenamerRouter = new DemoRenamerRouter(new DefaultDemoRenamer())
                .registerRenamer(new FaceitDemoRenamer());

        WorkspaceManager workspaceManager = new WorkspaceManager();

        MatchProcessor matchProcessor = new MatchProcessor(
                workspaceManager, matchContextProviderRouter, fileDownloader, demoExtractorRouter, demoRenamerRouter, demoFileManager
        );
    }
}
