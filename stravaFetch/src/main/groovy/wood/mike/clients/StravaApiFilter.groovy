package wood.mike.clients

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import wood.mike.annotations.StravaAuth
import wood.mike.services.StravaTokenService

@CompileStatic
@StravaAuth
@Singleton
@Requires(property = "strava.client-id")
@Requires(property = "strava.client-secret")
class StravaApiFilter implements HttpClientFilter{

    @Inject
    StravaTokenService stravaTokenService

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        chain.proceed(request.bearerAuth( stravaTokenService.getToken() ))
    }
}
