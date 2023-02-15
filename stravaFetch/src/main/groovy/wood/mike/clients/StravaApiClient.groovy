package wood.mike.clients

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import wood.mike.ActivityDto
import wood.mike.AthleteDto
import wood.mike.annotations.StravaAuth
import wood.mike.config.StravaApiConfig

import javax.validation.constraints.NotNull

@Client(StravaApiConfig.STRAVA_API_URL)
@StravaAuth
interface StravaApiClient {

    @Get('/athlete')
    Publisher<AthleteDto> fetchAthlete()

    @Get('/activities/{activityId}')
    Publisher<ActivityDto> fetchActivity(@PathVariable @Nullable String activityId)

    @Get('/activities')
    Flux<ActivityDto> fetchActivities(@QueryValue @Nullable Integer page, @QueryValue(value = 'per_page') @Nullable Integer perPage )

    @Get( '/activities' )
    Flux<ActivityDto> activitesAfter( @QueryValue @NotNull Long after )
}
