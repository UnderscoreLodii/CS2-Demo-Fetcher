package core.naming;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoRenamer {

    public Map<Path, String> generateNameMap(List<Path> demoFilePaths, String matchpageUrl, String customName) {
        Map<Path, String> nameMap = new HashMap<Path, String>();

        boolean useDefaultName = customName == null;

        boolean multipleFiles = demoFilePaths.size() > 1;

        for(Path demoFilePath : demoFilePaths) {

        }
    }
}
