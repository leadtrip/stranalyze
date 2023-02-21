package wood.mike

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.views.View
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wood.mike.clients.StravaStoreClient

@Controller("/stravaView")
class StravaViewController {

    private static Logger logger = LoggerFactory.getLogger(StravaViewController.class);

    private final StravaStoreClient stravaStoreClient

    StravaViewController( StravaStoreClient ssc ) {
        this.stravaStoreClient = ssc
    }

    @View("home")
    @Get("/")
    @ExecuteOn( TaskExecutors.IO )
    HttpResponse index() {
        return HttpResponse.ok([activities: stravaStoreClient.fetchActivities().collectList().block()])
    }

    @View("activity")
    @Get("/activity/{activityId}")
    @ExecuteOn( TaskExecutors.IO )
    HttpResponse activity(@PathVariable String activityId) {
        logger.info("Fetching activity with ID $activityId")
        def activity = stravaStoreClient.activity(activityId).block()
        return HttpResponse.ok([activity: activity])
    }
}
