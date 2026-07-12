package core.naming.renamers;

import core.models.MatchContext;

import java.nio.file.Path;
import java.util.List;

public class FaceitDemoRenamer extends AbstractDemoRenamer{

    @Override
    protected String generateDefaultName(MatchContext matchContext) {
        return "";
    }

    @Override
    protected List<Path> sortDemoFiles(List<Path> rawFiles, MatchContext matchContext) {
        return List.of();
    }

    @Override
    public String getSupportedSiteHost() {
        return "www.faceit.com";
    }
}
