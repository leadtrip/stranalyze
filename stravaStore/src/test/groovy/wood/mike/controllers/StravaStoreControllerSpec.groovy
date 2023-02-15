package wood.mike.controllers

import io.micronaut.context.annotation.Value
import io.micronaut.core.io.Readable
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification
import wood.mike.clients.StravaFetchClient
import wood.mike.domain.ActivityEntity
import wood.mike.domain.AthleteEntity
import wood.mike.repositories.ActivityRepository
import wood.mike.repositories.AthleteRepository

import java.time.LocalDateTime
import java.time.Month

@MicronautTest
class StravaStoreControllerSpec extends Specification{

    static final String ATHLETE_STRING =
            '''{
                "id": 3241,
                "firstname": "Chester",
                "lastname": "Drawers",
                "city": "Didcot",
                "state": "England",
                "country": "United Kingdom",
                "sex": "M"
            }'''

    @Inject
    ObjectMapper objectMapper

    @Inject
    StravaFetchClient stravaFetchClient

    @Inject
    @Client("/")
    ReactorStreamingHttpClient client

    @Inject
    AthleteRepository athleteRepository

    @Inject
    ActivityRepository activityRepository

    @Value("classpath:testactivities.json")
    Readable readable

    static final ACTIVITY_8455841466 = '8455841466'

    void "test fetchAthlete" () {
        when:
            client.toBlocking().exchange(HttpRequest.GET('/stravaStore/athlete'))
        then:
            stravaFetchClient.fetchAthlete() >> Mono.just( objectMapper.readValue(ATHLETE_STRING, AthleteEntity) )
        and:
            def athleteFromDb = athleteRepository.find()
            athleteFromDb.isPresent()
            athleteFromDb.get().firstname == 'Chester'
    }

    void "test activity operations" () {
        given:
            readable.exists()
            List<ActivityEntity> mockActivities = activityList()
            HttpRequest<?> request = HttpRequest.GET('/stravaStore/bulkloadactivities/1')
        when:
            client.toBlocking().exchange(request)
        then:
            stravaFetchClient.fetchActivities(_, _) >> Flux.fromIterable(mockActivities)
        and:
            activityRepository.findAll().size() == mockActivities.size()
        when:
            URI sportTypeUri = UriBuilder.of("/stravaStore").path('activitiesForSportType' ).queryParam('sportType', 'VirtualRide').build()
            request = HttpRequest.GET(sportTypeUri.toString())
            def response = client.jsonStream( request, ActivityEntity ).collectList().block()
        then:
            response.size() == 5
        when:
            LocalDateTime after = LocalDateTime.of(2023, Month.JANUARY, 27, 0, 0, 0)
            URI activitiesAfterUri = UriBuilder.of('/stravaStore').path('activitiesAfter').queryParam( 'after', after ).build()
            request = HttpRequest.GET( activitiesAfterUri.toString() )
            response = client.jsonStream( request, ActivityEntity ).collectList().block()
        then:
            response.size() == 6
        when:
            URI singleActivityUri = UriBuilder.of('/stravaStore').path('activity').path( ACTIVITY_8455841466 ).build()
            request = HttpRequest.GET( singleActivityUri.toString() )
            response = client.jsonStream( request, ActivityEntity ).blockFirst()
        then:
            response.name == 'Afternoon Run'
            response.distance == 5243
        when:
            request = HttpRequest.GET('/stravaStore/activities')
            response = client.jsonStream( request, ActivityEntity ).collectList().block()
        then:
            response.size() == mockActivities.size()
    }

    def activityList() {
        objectMapper.readValue( readable.asInputStream(), Argument.listOf(ActivityEntity.class) )
    }

    @MockBean(StravaFetchClient)
    StravaFetchClient stravaFetchClient() {
        Mock(StravaFetchClient)
    }
}
