package core.models;

import java.util.List;

/**
 * An immutable data transfer object (DTO) that holds metadata and download links
 * extracted from a match room URL. This record acts as the bridge between the
 * network layer and the local file processing pipeline, ensuring data remains
 * strictly read-only once fetched.
 *
 * @param siteHost  The domain or host from which the match data was retrieved.
 * @param demo_urls A list of direct download links for the match's raw demo files (.gz or .zip).
 * @param team_A    The extracted name of the first team (may be null if unsupported by the platform).
 * @param team_B    The extracted name of the second team (may be null if unsupported by the platform).
 */
public record MatchContext(
        String siteHost,
        List<String> demo_urls,
        String team_A,
        String team_B
) {
}
