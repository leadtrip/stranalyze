package wood.mike.jobs

import groovy.transform.CompileStatic
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import wood.mike.services.DataLoadService

@CompileStatic
@Singleton
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
