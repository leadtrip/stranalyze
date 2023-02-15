package wood.mike.controllers

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import wood.mike.ActivityDto
import wood.mike.AthleteDto
import wood.mike.services.ActivityService
import wood.mike.services.AthleteService

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

@CompileStatic
@Controller("/stravaStore")
class StravaStoreController {

    private static Logger logger = LoggerFactory.getLogger(StravaStoreController.class)

    private final ActivityService activityService
    private final AthleteService athleteService

    StravaStoreController(ActivityService acs, AthleteService athleteService ) {
        this.activityService = acs
        this.athleteService = athleteService
    }

    /**
     * Return the athlete from the database if present, otherwise fetch from strava, persist and return
     */
    @Get(uri = "/athlete", produces = MediaType.APPLICATION_JSON_STREAM)
    Mono<AthleteDto> fetchAthlete() {
        logger.info("Fetching athlete")
        athleteService.fetchAthlete()
    }

    /**
     * Return the given activity from the database if present, otherwise fetch from strava, persist and return
     * @param activityId    - the strava activity ID
     */
    @Get(uri = "/activity/{activityId}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> fetchActivity(@PathVariable String activityId) {
        logger.info("Fetching activity ${activityId}")
        activityService.fetchActivity(activityId)
    }

    /**
     * @return all activities from the database
     */
    @Get(uri = "/activities", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> fetchActivities() {
        logger.info("Fetching all activities")
        activityService.fetchActivities()
    }

    /**
     * @param after - date time to fetch activities after
     * @return activities from the database after the given date time
     */
    @Get( uri = "/activitiesAfter", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<ActivityDto> activitiesAfter(@QueryValue @NotNull LocalDateTime after) {
        logger.info("Fetching activities after ${after}")
        activityService.activitiesAfter(after)
    }

    /**
     * @param sportType - Strava defined sport type
     * @return activities for the given sport type
     */
    @Get( uri = "/activitiesForSportType", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<ActivityDto> activitiesForSportType(@QueryValue @NotNull String sportType) {
        logger.info("Fetching activites for sport type ${sportType}")
        activityService.activitiesForSportType(sportType)
    }

    /**
     * Fetch activities from strava fetch and persist if not already present
     * @param page  - the page number where 1 is the most recent, used in combination with max per page config setting
     */
    @Get(uri = "/bulkloadactivities/{page}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> bulkLoadActivities(@PathVariable Integer page) {
        activityService.bulkLoadActivities(page)
    }

    /**
     * @param activityId    - the activity ID
     * @return a text based activity summary response
     */
    @Get(uri = '/activitySummary/{activityId}', produces = MediaType.TEXT_PLAIN )
    Mono<String> activitySummary( @PathVariable String activityId ) {
        activityService.activitySummary( activityId )
    }

    /**
     * @return a summarized version of the latest activity
     */
    @Get(uri = '/latestActivity', produces = MediaType.TEXT_PLAIN )
    Mono<String> latestActivity() {
        logger.info("Getting latest activity")
        activityService.latestActivity()
    }

    /**
     * @return count of all activities
     */
    @Get(uri = '/activityCount', produces = MediaType.TEXT_PLAIN )
    Mono<Long> activityCount() {
        logger.info("Getting activity count")
        activityService.activityCount()
    }

    @Get(uri = '/loadLatestActivites', produces = MediaType.TEXT_PLAIN)
    Mono<Long> loadLatestActivites() {
        logger.info("Loading latest activities")
        activityService.loadLatestActivities()
    }
}
