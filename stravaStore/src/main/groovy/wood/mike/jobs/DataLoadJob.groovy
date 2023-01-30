package wood.mike.jobs

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import wood.mike.services.DataLoadService

/**
 * Job that pulls activities from Strava into the local database.
 * Strava does offer a webhook which will inform of new activities but this requires a public facing URL.
 */
@CompileStatic
@Singleton
@Requires(notEnv="test")
class DataLoadJob {

    private final DataLoadService dataLoadService

    DataLoadJob( DataLoadService dls ) {
        dataLoadService = dls
    }

    @Scheduled( initialDelay = "2s", fixedDelay = "5m" )
    void execute() {
        dataLoadService.loadLatestActivites()
    }
}
