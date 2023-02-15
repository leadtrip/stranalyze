package wood.mike.controllers

import groovy.transform.CompileStatic
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.serde.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import wood.mike.ActivityDto
import wood.mike.AthleteDto
import wood.mike.clients.StravaFetchClient
import wood.mike.config.StravaFetchConfig
import wood.mike.domain.ActivityEntity
import wood.mike.domain.ActivitySummary
import wood.mike.domain.AthleteEntity
import wood.mike.repositories.ActivityRepository
import wood.mike.repositories.AthleteRepository
import wood.mike.services.ActivityTransformer
import wood.mike.services.AthleteTransformer

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
    private final ObjectMapper objectMapper

    StravaStoreController(StravaFetchConfig conf,
                          StravaFetchClient sfc,
                          AthleteRepository ar,
                          ActivityRepository actR,
                          ObjectMapper om) {
        this.stravaFetchConfig = conf
        this.stravaFetchClient = sfc
        this.athleteRepository = ar
        this.activityRepository = actR
        this.objectMapper = om
    }

    /**
     * Return the athlete from the database if present, otherwise fetch from strava, persist and return
     */
    @Get(uri = "/athlete", produces = MediaType.APPLICATION_JSON_STREAM)
    Mono<AthleteDto> fetchAthlete() {
        logger.info("Fetching athlete")
        Optional<AthleteEntity> athlete = athleteRepository.find()
        if( athlete.isPresent() ) {
            return Mono<AthleteDto>.just(AthleteTransformer.from(athlete.get()))
        }
        stravaFetchClient.fetchAthlete()
            .map(AthleteTransformer::to)
                .map(athleteRepository::persist)
                    .map ( AthleteTransformer::from )
    }

    /**
     * Return the given activity from the database if present, otherwise fetch from strava, persist and return
     * @param activityId    - the strava activity ID
     */
    @Get(uri = "/activity/{activityId}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> fetchActivity(@PathVariable String activityId) {
        logger.info("Fetching activity ${activityId}")
        Optional<ActivityEntity> activity = activityRepository.findByStravaActivityId(activityId as Long)
        if (activity.isPresent()) {
            logger.info("Activity ${activityId} found in database")
            return Flux<ActivityDto>.just(ActivityTransformer.from(activity.get()))
        }
        stravaFetchClient.fetchActivity(activityId)
            .map( ActivityTransformer::to )
                .map( ActivityTransformer::from )
    }

    /**
     * @return all activities from the database
     */
    @Get(uri = "/activities", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> fetchActivities() {
        logger.info("Fetching all activities")
        Flux<ActivityDto>.fromIterable(activityRepository.findAll())
            .map(ActivityTransformer::from)
    }

    /**
     * @param after - date time to fetch activities after
     * @return activities from the database after the given date time
     */
    @Get( uri = "/activitiesAfter", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<ActivityDto> activitiesAfter(@QueryValue @NotNull LocalDateTime after) {
        logger.info("Fetching activities after ${after}")
        Flux<ActivityDto>.fromIterable( activityRepository.findAllByStartDateAfter( after ) )
            .map( ActivityTransformer::from )
    }

    /**
     * @param sportType - Strava defined sport type
     * @return activities for the given sport type
     */
    @Get( uri = "/activitiesForSportType", produces = MediaType.APPLICATION_JSON_STREAM )
    Flux<ActivityDto> activitiesForSportType(@QueryValue @NotNull String sportType) {
        logger.info("Fetching activites for sport type ${sportType}")
        Flux<ActivityDto>.fromIterable( activityRepository.findAllBySportTypeIlike( sportType ) )
            .map( ActivityTransformer::from )
    }

    /**
     * Fetch activities from strava fetch and persist if not already present
     * @param page  - the page number where 1 is the most recent, used in combination with max per page config setting
     */
    @Get(uri = "/bulkloadactivities/{page}", produces = MediaType.APPLICATION_JSON_STREAM)
    Flux<ActivityDto> bulkLoadActivities(@PathVariable Integer page) {
        logger.info("Loading page ${page} of activities with max per page of ${stravaFetchConfig.maxPerPage}")

        stravaFetchClient.fetchActivities(page, stravaFetchConfig.maxPerPage)
                .map( ActivityTransformer::to )
                .filter(activity -> !activityRepository.findByStravaActivityId(activity.id).isPresent())
                .map(activityRepository::persist)
                .map(ActivityTransformer::from)
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
                    Mono.just( activityMinimal(activity) ) )
        .switchIfEmpty( Mono.just( "Activity ID ${activityId} not found".toString() ) )
    }

    /**
     * @return a summarized version of the latest activity
     */
    @Get(uri = '/latestActivity', produces = MediaType.TEXT_PLAIN )
    Mono<String> latestActivity() {
        logger.info("Getting latest activity")
        Mono.justOrEmpty( activityRepository.findLatestActivity() )
                .flatMap( activity ->
                        Mono.just( activityMinimal( activity ) ) )
                .switchIfEmpty( Mono.just( "No activities found" ) )
    }

    /**
     * @return count of all activities
     */
    @Get(uri = '/activityCount', produces = MediaType.TEXT_PLAIN )
    Mono<Long> activityCount() {
        logger.info("Getting activity count")
        Mono.just(activityRepository.count())
    }

    /**
     * @param activity  - the activity
     * @return a summarized version of the activity
     */
    String activityMinimal(ActivityEntity activity ) {
        objectMapper.writeValueAsString( new ActivitySummary(activity) )
    }
}
