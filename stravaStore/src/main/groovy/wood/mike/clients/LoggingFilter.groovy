package wood.mike.clients

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
@CompileStatic
@Filter(Filter.MATCH_ALL_PATTERN)
class LoggingFilter implements HttpClientFilter{

    private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        logger.info("Sending request ${request}")
        chain.proceed(request)
    }

}
