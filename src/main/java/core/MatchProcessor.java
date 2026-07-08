package core;

import core.naming.DemoRenamer;
import io.DemoExtractorRouter;
import io.DemoFileManager;
import network.DemoURLProviderRouter;
import network.FileDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MatchProcessor {
    private final DemoURLProviderRouter demoURLProviderRouter;
    private final FileDownloader  fileDownloader;
    private final DemoExtractorRouter demoExtractorRouter;
    private final DemoRenamer demoRenamer;
    private final DemoFileManager  demoFileManager;

    public MatchProcessor(DemoURLProviderRouter demoURLProviderRouter, FileDownloader fileDownloader, DemoExtractorRouter demoExtractorRouter, DemoRenamer demoRenamer, DemoFileManager demoFileManager){
        this.demoURLProviderRouter = demoURLProviderRouter;
        this.fileDownloader = fileDownloader;
        this.demoExtractorRouter = demoExtractorRouter;
        this.demoRenamer = demoRenamer;
        this.demoFileManager = demoFileManager;
    }

    public void processMatch(String matchpageUrl, Path tempDirPath, Path csDemoDirPath, String customName) throws IllegalArgumentException, IOException {

        String downloadUrl = demoURLProviderRouter.getDownloadLink(matchpageUrl);

        Path downloadedFilePath = fileDownloader.downloadFile(tempDirPath, downloadUrl);

        List<Path> demoFilePaths = demoExtractorRouter.extractDemoFiles(downloadedFilePath);

        Map<Path, String> nameMap = demoRenamer.generateNameMap(demoFilePaths, matchpageUrl, customName);

        for(Map.Entry<Path, String> entry : nameMap.entrySet()){
            Path path = entry.getKey();
            String finalName = entry.getValue();

            demoFileManager.manageFile(path, csDemoDirPath, finalName);
        }
    }
}
