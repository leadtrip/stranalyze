package wood.mike.clients

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Flux
import wood.mike.model.Activity

import static io.micronaut.http.HttpHeaders.ACCEPT

@Client('http://localhost:10051/stravaStore')
@Header(name = ACCEPT, value = MediaType.APPLICATION_JSON_STREAM )
interface StravaStoreClient {

    @Get('/activities')
    Flux<Activity> fetchActivities()
}
