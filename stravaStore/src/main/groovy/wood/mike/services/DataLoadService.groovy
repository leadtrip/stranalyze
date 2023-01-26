package wood.mike.services

import groovy.util.logging.Slf4j
import jakarta.inject.Singleton
import wood.mike.clients.StravaFetchClient
import wood.mike.config.StravaFetchConfig
import wood.mike.domain.Activity
import wood.mike.repositories.ActivityRepository

import java.time.LocalDateTime
import java.time.ZoneOffset

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

        Optional<Long> maxId = activityRepository.findMaxId()

        LocalDateTime lastActivityStart = maxId.isPresent()
                ? activityRepository.findByStravaActivityId( maxId.get() ).get().startDate
                : LocalDateTime.now().minusMonths(1)

        log.info("Polling for activites created after ${lastActivityStart}")

        List<Activity> activities = stravaFetchClient.activitiesAfter( lastActivityStart.toEpochSecond(ZoneOffset.UTC) ).collectList().block()

        log.info("Got ${activities.size()} activities")

        activities
            .findAll{ activity -> !activityRepository.findByStravaActivityId(activity.id).isPresent() }
            .each {activity ->
                log.info("Adding activity " + activity.id);
                activityRepository.persist(activity)
            }
    }
}
