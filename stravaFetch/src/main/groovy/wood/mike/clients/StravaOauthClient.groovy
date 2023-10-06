package wood.mike.clients

import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import wood.mike.config.StravaApiConfig
import wood.mike.model.StravaRefreshToken

@Client(StravaApiConfig.STRAVA_API_URL)
interface StravaOauthClient {

    /**
     * TODO, the refreshToken is currently being taken from config, this should be taken from the cached value this call fetches
     */
    @Post('/oauth/token?client_id=${strava.client-id}&client_secret=${strava.client-secret}&grant_type=refresh_token&refresh_token=${strava.refresh-token}')
    StravaRefreshToken fetchRefreshToken()
}
