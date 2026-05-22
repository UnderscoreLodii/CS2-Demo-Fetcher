package network;

import network.providers.DemoProvider;

import java.net.URI;
import java.util.HashMap;

public class DemoRouter {
    private final HashMap<String, DemoProvider> providers = new HashMap<>();

    public DemoRouter registerProvider(DemoProvider provider){
        providers.put(provider.getProviderSiteHost(), provider);
        return this;
    }

    public String getDownloadLink(String matchPageUrl) throws IllegalArgumentException {
        URI uri = URI.create(matchPageUrl);
        DemoProvider provider = providers.get(uri.getHost());
        if (provider != null) {
            return provider.getDownloadLink(uri);
        }
        else throw new IllegalArgumentException("Unsupported host " + uri.getHost());
    }
}
