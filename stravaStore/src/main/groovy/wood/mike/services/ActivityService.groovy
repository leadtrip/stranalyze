package wood.mike.services

import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import wood.mike.ActivityDto
import wood.mike.clients.StravaFetchClient
import wood.mike.config.StravaFetchConfig
import wood.mike.domain.ActivityEntity
import wood.mike.domain.ActivitySummary
import wood.mike.repositories.ActivityRepository

import java.time.LocalDateTime

@Singleton
class ActivityService {

    private static Logger logger = LoggerFactory.getLogger(ActivityService.class)

    private final StravaFetchConfig stravaFetchConfig
    private final StravaFetchClient stravaFetchClient
    private final ActivityRepository activityRepository
    private final ObjectMapper objectMapper

    ActivityService(StravaFetchConfig stravaFetchConfig,
                    StravaFetchClient stravaFetchClient,
                    ActivityRepository activityRepository,
                    ObjectMapper objectMapper) {
        this.stravaFetchConfig = stravaFetchConfig
        this.stravaFetchClient = stravaFetchClient
        this.activityRepository = activityRepository
        this.objectMapper = objectMapper
    }

    Flux<ActivityDto> fetchActivity(String activityId) {
        Optional<ActivityEntity> activity = activityRepository.findByStravaActivityId(activityId as Long)
        if (activity.isPresent()) {
            logger.info("Activity ${activityId} found in database")
            return Flux<ActivityDto>.just(ActivityTransformer.from(activity.get()))
        }
        stravaFetchClient.fetchActivity(activityId)
                .map( ActivityTransformer::to )
                .map( ActivityTransformer::from )
    }

    Flux<ActivityDto> fetchActivities() {
        Flux<ActivityDto>.fromIterable(activityRepository.findAll())
                .map(ActivityTransformer::from)
    }

    Flux<ActivityDto> activitiesAfter( LocalDateTime after ) {
        Flux<ActivityDto>.fromIterable( activityRepository.findAllByStartDateAfter( after ) )
                .map( ActivityTransformer::from )
    }

    Flux<ActivityDto> activitiesForSportType( String sportType ) {
        Flux<ActivityDto>.fromIterable( activityRepository.findAllBySportTypeIlike( sportType ) )
                .map( ActivityTransformer::from )
    }

    Flux<ActivityDto> bulkLoadActivities( Integer page ) {
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

    Mono<String> activitySummary( String activityId ) {
        Mono.justOrEmpty( activityRepository.findByStravaActivityId(activityId as Long) )
                .flatMap( activity ->
                        Mono.just( activityMinimal(activity) ) )
                .switchIfEmpty( Mono.just( "Activity ID ${activityId} not found".toString() ) )
    }

    Mono<String> latestActivity() {
        Mono.justOrEmpty( activityRepository.findLatestActivity() )
                .flatMap( activity ->
                        Mono.just( activityMinimal( activity ) ) )
                .switchIfEmpty( Mono.just( "No activities found" ) )
    }

    Mono<Long> activityCount() {
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
