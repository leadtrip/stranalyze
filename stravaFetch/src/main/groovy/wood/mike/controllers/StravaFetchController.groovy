package wood.mike.controllers

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wood.mike.ActivityDto
import wood.mike.AthleteDto
import wood.mike.clients.StravaApiClient

@CompileStatic
@Controller("/stravaFetch")
class StravaFetchController {

    private static Logger logger = LoggerFactory.getLogger(StravaFetchController.class);

    private final StravaApiClient stravaApiClient

    StravaFetchController(StravaApiClient sac) {
        this.stravaApiClient = sac
    }

    @Get(uri = "/athlete", produces = [MediaType.APPLICATION_JSON_STREAM, MediaType.APPLICATION_JSON])
    Publisher<AthleteDto> fetchAthlete() {
        logger.info("Fetching athlete")
        stravaApiClient.fetchAthlete()
    }

    @Get(uri = "/activity/{activityId}", produces = [MediaType.APPLICATION_JSON_STREAM, MediaType.APPLICATION_JSON])
    Publisher<ActivityDto> fetchActivity(@PathVariable String activityId) {
        logger.info("Fetching activity ${activityId}")
        stravaApiClient.fetchActivity(activityId)
    }

    @Get(uri = "/activities", produces = [MediaType.APPLICATION_JSON_STREAM, MediaType.APPLICATION_JSON])
    Publisher<ActivityDto> getActivities(@QueryValue @Nullable Integer page, @QueryValue(value = 'per_page') @Nullable Integer perPage ) {
        logger.info("Fetching activities")
        stravaApiClient.fetchActivities(page, perPage)
    }

    @Get(uri = "/activitiesAfter", produces = [MediaType.APPLICATION_JSON_STREAM, MediaType.APPLICATION_JSON])
    Publisher<ActivityDto> activitiesAfter(@QueryValue @Nullable Long after ) {
        logger.info("Fetching activities after ${after}")
        stravaApiClient.activitesAfter( after )
    }

}
