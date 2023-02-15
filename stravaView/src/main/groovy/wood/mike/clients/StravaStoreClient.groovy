package wood.mike.clients


import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Flux
import wood.mike.ActivityDto

import static io.micronaut.http.HttpHeaders.ACCEPT

@Client('http://localhost:10051/stravaStore')
@Header(name = ACCEPT, value = MediaType.APPLICATION_JSON_STREAM )
interface StravaStoreClient {

    @Get('/activities')
    Flux<ActivityDto> fetchActivities()
}
