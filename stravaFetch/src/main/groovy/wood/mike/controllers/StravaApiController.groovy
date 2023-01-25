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
import wood.mike.clients.StravaApiClient
import wood.mike.model.Activity
import wood.mike.model.Athlete

@CompileStatic
@Controller("/stravaFetch")
class StravaApiController {

    private static Logger logger = LoggerFactory.getLogger(StravaApiController.class);

    private final StravaApiClient stravaApiClient

    StravaApiController(StravaApiClient sac) {
        this.stravaApiClient = sac
    }

    @Get(uri = "/athlete", produces = MediaType.APPLICATION_JSON_STREAM)
    Publisher<Athlete> fetchAthlete() {
        logger.info("Fetching athlete")
        stravaApiClient.fetchAthlete()
    }

    @Get(uri = "/activity/{activityId}", produces = MediaType.APPLICATION_JSON_STREAM)
    Publisher<Activity> fetchActivity(@PathVariable String activityId) {
        logger.info("Fetching activity ${activityId}")
        stravaApiClient.fetchActivity(activityId)
    }

    @Get(uri = "/activities", produces = MediaType.APPLICATION_JSON_STREAM)
    Publisher<Activity> getActivities(@QueryValue @Nullable page, @QueryValue(value = 'per_page') @Nullable Integer perPage ) {
        logger.info("Fetching activities")
        stravaApiClient.fetchActivities(page, perPage)
    }
}