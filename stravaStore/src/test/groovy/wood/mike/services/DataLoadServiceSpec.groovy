package wood.mike.services

import io.micronaut.serde.ObjectMapper
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import spock.lang.Specification
import wood.mike.clients.StravaFetchClient
import wood.mike.domain.Activity
import wood.mike.repositories.ActivityRepository

@MicronautTest
class DataLoadServiceSpec extends Specification {

    static final String ACTIVITY_1 =
            '''{
                "id": 8437473425,
                "name": "Xert - Lucy in the sky 90",
                "distance": 47552,
                "moving_time": 5405,
                "elapsed_time": 5405,
                "total_elevation_gain": 407,
                "sport_type": "VirtualRide",
                "start_date": 1674457341000,
                "timezone": "(GMT+00:00) Europe/London",
                "average_speed": 8.8,
                "max_speed": 19,
                "average_cadence": 96.8,
                "average_watts": 214.8,
                "weighted_average_watts": 218,
                "kilojoules": 1161.2,
                "average_heartrate": 142.6,
                "max_heartrate": 155,
                "max_watts": 251,
                "suffer_score": 74
            }'''
        static final String ACTIVITY_2 =
            '''{
                "id": 8434177290,
                "name": "Afternoon Run",
                "distance": 9990,
                "moving_time": 3662,
                "elapsed_time": 3674,
                "total_elevation_gain": 29,
                "sport_type": "Run",
                "start_date": 1674396674000,
                "timezone": "(GMT+00:00) Europe/London",
                "average_speed": 2.7,
                "max_speed": 3,
                "average_cadence": 85.3,
                "average_heartrate": 137.7,
                "max_heartrate": 153,
                "suffer_score": 21
            }'''

    @Inject
    ObjectMapper objectMapper

    @Inject
    StravaFetchClient stravaFetchClient

    @Inject
    DataLoadService dataLoadService

    @Inject
    ActivityRepository activityRepository

    void "test loadLatestActivites"() {
        given:
            def aList =
                    [objectMapper.readValue(ACTIVITY_1, Activity),
                     objectMapper.readValue(ACTIVITY_2, Activity)]
        when:
            dataLoadService.loadLatestActivites()
        then:
            stravaFetchClient.activitiesAfter(_) >> Flux.fromIterable( aList )
        then:
            activityRepository.findAll().size() == 2
    }

    @MockBean(StravaFetchClient)
    StravaFetchClient stravaFetchClient() {
        Mock(StravaFetchClient)
    }
}
