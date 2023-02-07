package wood.mike.controllers

import groovy.transform.CompileStatic
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import wood.mike.clients.StravaFetchClient
import wood.mike.config.StravaFetchConfig
import wood.mike.domain.Activity
import wood.mike.domain.Athlete
import wood.mike.repositories.ActivityRepository
import wood.mike.repositories.AthleteRepository

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

@CompileStatic
@Controller("/stravaStore")
class StravaStoreController {

    private static Logger logger = LoggerFactory.getLogger(StravaStoreController.class);

    private final StravaFetchConfig stravaFetchConfig
    private final StravaFetchClient stravaFetchClient
    private final AthleteRepository athleteRepository
    private final ActivityRepository activityRepository

    StravaStoreController(StravaFetchConfig conf,
                          StravaFetchClient sfc,
                          AthleteRepository ar,
                          ActivityRepository actR) {
        this.stravaFetchConfig = conf
        this.stravaFetchClient = sfc
        this.athleteRepository = ar
        this.activityRepository = actR
    }

    /**
     * Return the athlete from the database if present, otherwise fetch from strava, persist and return
     */
    @Get(uri = "/athlete", produces = MediaType.APPLICATION_JSON_STREAM)
    Mono<Athlete> fetchAthlete() {
        logger.info("Fetching athlete")
        Optional<Athlete> athlete = athleteRepository.find()
        if( athlete.isPresent() ) {
            return Mono<Athlete>.just(athlete.get())
        }
        stravaFetchClient.fetchAthlete().map(athleteRepository::persist)
    }

    /**
     * Return the given activity from the database if present, otherwise fetch from strava, persist and return
     * @param activityId    - the strava activity ID
     */
    @Get(uri = "/activity/{activityId}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<Activity> fetchActivity(@PathVariable String activityId) {
        logger.info("Fetching activity ${activityId}")
        Optional<Activity> activity = activityRepository.findByStravaActivityId(activityId as Long)
        if (activity.isPresent()) {
            logger.info("Activity ${activityId} found in database")
            return Flux<Activity>.just(activity.get())
        }
        stravaFetchClient.fetchActivity(activityId)
                .map(activityRepository::persist)
    }

    /**
     * @return all activities from the database
     */
    @Get(uri = "/activities", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<Activity> fetchActivities() {
        logger.info("Fetching all activities")
        Flux<Activity>.fromIterable(activityRepository.findAll())
    }

    /**
     * @param after - date time to fetch activities after
     * @return activities from the database after the given date time
     */
    @Get( uri = "/activitiesAfter", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<Activity> activitiesAfter( @QueryValue @NotNull LocalDateTime after) {
        logger.info("Fetching activities after ${after}")
        Flux<Activity>.fromIterable( activityRepository.findAllByStartDateAfter( after ) )
    }

    /**
     * @param sportType - Strava defined sport type
     * @return activities for the given sport type
     */
    @Get( uri = "/activitiesForSportType", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<Activity> activitiesForSportType( @QueryValue @NotNull String sportType) {
        logger.info("Fetching activites for sport type ${sportType}")
        Flux<Activity>.fromIterable( activityRepository.findAllBySportTypeIlike( sportType ) )
    }

    /**
     * Fetch activities from strava fetch and persist if not already present
     * @param page  - the page number where 1 is the most recent, used in combination with max per page config setting
     */
    @Get(uri = "/bulkloadactivities/{page}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<Activity> bulkLoadActivities(@PathVariable Integer page) {
        logger.info("Loading page ${page} of activities with max per page of ${stravaFetchConfig.maxPerPage}")

        stravaFetchClient.fetchActivities(page, stravaFetchConfig.maxPerPage)
                .filter(activity -> !activityRepository.findByStravaActivityId(activity.id).isPresent())
                .map(activityRepository::persist)
                .onErrorContinue(DataAccessException.class, (throwable, o) -> {
                    logger.error("Error while processing {}. Cause: {}", o, throwable.getMessage())
                })
    }

    /**
     * @param activityId    - the activity ID
     * @return a text based activity summary response
     */
    @Get(uri = '/activitySummary/{activityId}', produces = MediaType.TEXT_PLAIN )
    Mono<String> activitySummary( @PathVariable String activityId ) {
        Mono.justOrEmpty( activityRepository.findByStravaActivityId(activityId as Long) )
            .flatMap( activity ->
                    Mono.just( "ID=${activity.id}, name=${activity.name}, distance=${activity.distance}, started=${activity.startDate}, duration=${activity.elapsed_time}".toString() ) )
        .switchIfEmpty( Mono.just( "Activity ID ${activityId} not found".toString() ) )
    }
}
