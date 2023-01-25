package wood.mike.clients

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import wood.mike.annotations.StravaAuth
import wood.mike.config.StravaApiConfig
import wood.mike.model.Activity
import wood.mike.model.Athlete

@Client(StravaApiConfig.STRAVA_API_URL)
@StravaAuth
interface StravaApiClient {

    @Get('/athlete')
    Publisher<Athlete> fetchAthlete()

    @Get('/activities/{activityId}')
    Publisher<Activity> fetchActivity(@PathVariable @Nullable String activityId)

    @Get('/activities')
    Flux<Activity> fetchActivities(@QueryValue @Nullable page, @QueryValue(value = 'per_page') @Nullable Integer perPage )

}
