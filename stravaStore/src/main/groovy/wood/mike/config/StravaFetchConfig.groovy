package wood.mike.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(PREFIX)
@Requires(property = PREFIX)
class StravaFetchConfig {
    public static final String PREFIX = "strava"
    Integer maxPerPage
}
