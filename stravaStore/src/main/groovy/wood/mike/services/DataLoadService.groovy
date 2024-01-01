package wood.mike.services

import groovy.util.logging.Slf4j
import jakarta.inject.Singleton
import wood.mike.ActivityDto
import wood.mike.clients.StravaFetchClient
import wood.mike.config.StravaFetchConfig
import wood.mike.repositories.ActivityRepository

import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Updates the database with new activities created after the current latest.
 * If the database is empty it'll be populated with a configurable number of months worth of activities.
 */
@Singleton
@Slf4j
class DataLoadService {

    private final StravaFetchConfig stravaFetchConfig
    private final StravaFetchClient stravaFetchClient
    private final ActivityRepository activityRepository

    DataLoadService(StravaFetchConfig conf,
                          StravaFetchClient sfc,
                          ActivityRepository actR) {
        this.stravaFetchConfig = conf
        this.stravaFetchClient = sfc
        this.activityRepository = actR
    }

    void loadLatestActivites() {
        log.info("Loading latest activities")

        LocalDateTime lastActivityStart =
                activityRepository.findMaxStartDate().orElse(LocalDateTime.now().minusMonths(stravaFetchConfig.emptyDbMonths))

        log.info("Polling for activites created after ${lastActivityStart}")

        List<ActivityDto> activities = stravaFetchClient.activitiesAfter( lastActivityStart.toEpochSecond(ZoneOffset.UTC) )
                .collectList()
                .block()

        log.info("Got ${activities.size()} activities")

        activities
                .findAll{ activity -> !activityRepository.findByStravaActivityId(activity.id).isPresent() }
                .each {activity ->
                    log.info("Adding activity " + activity.id);
                    activityRepository.persist(ActivityTransformer.to(activity))
            }

        // Load an empty database with some activities and top up until latest is present
        if ( activities.size() >= stravaFetchConfig.maxPerPage) {
            loadLatestActivites()
        }
    }
}
