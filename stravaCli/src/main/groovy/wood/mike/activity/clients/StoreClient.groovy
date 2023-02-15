package wood.mike.activity.clients

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

import static io.micronaut.http.HttpHeaders.ACCEPT

@Client('http://localhost:10051/stravaStore')
public interface StoreClient {

    @Header(name = ACCEPT, value = MediaType.TEXT_PLAIN )
    @Get( '/activityCount' )
    Mono<Long> activityCount()

    @Header(name = ACCEPT, value = MediaType.TEXT_PLAIN )
    @Get( '/latestActivity' )
    Mono<String> latestActivity()

    @Header(name = ACCEPT, value = MediaType.TEXT_PLAIN )
    @Get( '/loadLatestActivites' )
    Mono<Long> loadLatestActivites()
}
