package wood.mike.services

import groovy.transform.CompileStatic
import io.micronaut.caffeine.cache.Cache
import io.micronaut.caffeine.cache.Caffeine
import io.micronaut.context.BeanProvider
import jakarta.inject.Singleton
import wood.mike.clients.StravaOauthClient
import wood.mike.config.StravaApiConfig
import wood.mike.model.StravaRefreshToken

@Singleton
@CompileStatic
class StravaTokenService {

    Cache<String, StravaRefreshToken> tokenCache
    StravaApiConfig stravaApiConfig
    BeanProvider<StravaOauthClient> stravaOauthClient

    StravaTokenService( StravaApiConfig conf, BeanProvider<StravaOauthClient> client ) {
        this.stravaApiConfig = conf
        this.stravaOauthClient = client
        tokenCache = Caffeine.newBuilder().build()
    }

    /**
     * Use the cached token if it exists and hasn't expired, otherwise fetch a new token, cache and return
     */
    String getToken() {
        StravaRefreshToken token = tokenCache.getIfPresent(stravaApiConfig.clientId)
        if ( token && new Date( token.expires_at * 1000 ).after( new Date() ) ) {
                return token.access_token
        }
        token = stravaOauthClient.get().fetchRefreshToken()
        tokenCache.put(stravaApiConfig.clientId, token)
        token.access_token
    }
}
