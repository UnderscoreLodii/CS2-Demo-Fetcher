package network.providers;

import java.net.URI;

public interface DemoProvider {
    String getProviderSiteHost();
    String getDownloadLink(URI uri);
}
