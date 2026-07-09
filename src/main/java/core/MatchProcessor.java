package core;

import core.models.MatchContext;
import core.naming.DemoRenamerRouter;
import io.DemoExtractorRouter;
import io.DemoFileManager;
import network.MatchContextProviderRouter;
import network.FileDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MatchProcessor {
    private final WorkspaceManager workspaceManager;
    private final MatchContextProviderRouter matchContextProviderRouter;
    private final FileDownloader  fileDownloader;
    private final DemoExtractorRouter demoExtractorRouter;
    private final DemoRenamerRouter demoRenamerRouter;
    private final DemoFileManager  demoFileManager;

    public MatchProcessor(WorkspaceManager workspaceManager, MatchContextProviderRouter matchContextProviderRouter, FileDownloader fileDownloader, DemoExtractorRouter demoExtractorRouter, DemoRenamerRouter demoRenamerRouter, DemoFileManager demoFileManager){
        this.workspaceManager = workspaceManager;
        this.matchContextProviderRouter = matchContextProviderRouter;
        this.fileDownloader = fileDownloader;
        this.demoExtractorRouter = demoExtractorRouter;
        this.demoRenamerRouter = demoRenamerRouter;
        this.demoFileManager = demoFileManager;
    }

    public void processMatch(String matchpageUrl, Path tempDirPath, Path csDemoDirPath, String customName) throws IllegalArgumentException, IOException {

        Path workspace = workspaceManager.createWorkspace();

        MatchContext matchContext = matchContextProviderRouter.getMatchContext(matchpageUrl);

        List<Path> downloadedFilePaths = fileDownloader.downloadFiles(workspace, matchContext.demo_urls());

        List<Path> demoFilePaths = demoExtractorRouter.extractAll(downloadedFilePaths);

        Map<Path, String> nameMap = demoRenamerRouter.generateNameMap(demoFilePaths, matchContext, customName);

        for(Map.Entry<Path, String> entry : nameMap.entrySet()){
            Path path = entry.getKey();
            String finalName = entry.getValue();

            demoFileManager.manageFile(path, csDemoDirPath, finalName);
        }

        workspaceManager.cleanupWorkspace();
    }
}
