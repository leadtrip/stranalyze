package wood.mike

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.views.View
import wood.mike.clients.StravaStoreClient

@Controller("/stravaView")
class StravaViewController {

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

}
