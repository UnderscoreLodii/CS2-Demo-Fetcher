package core.naming.renamers;

import java.nio.file.Path;
import java.util.List;

public class FaceitDemoRenamer extends AbstractDemoRenamer{

    @Override
    protected String generateDefaultName(String matchpageUrl) {
        return "";
    }

    @Override
    protected List<Path> sortDemoFiles(List<Path> rawFiles, String matchpageUrl) {
        return List.of();
    }

    @Override
    public String getSupportedSiteHost() {
        return "www.faceit.com";
    }
}
