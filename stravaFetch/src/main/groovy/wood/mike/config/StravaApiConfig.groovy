package wood.mike.config

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(PREFIX)
@Requires(property = PREFIX)
@CompileStatic
class StravaApiConfig {
    public static final String PREFIX = "strava"
    public static final String STRAVA_API_URL = "https://www.strava.com/api/v3"

    String clientId
    String clientSecret
    String refreshToken
}
