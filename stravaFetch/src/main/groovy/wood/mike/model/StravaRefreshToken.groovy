package wood.mike.model

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
class StravaRefreshToken {
    String token_type
    String access_token
    String refresh_token
    Long expires_at
    Long expires_in
}
