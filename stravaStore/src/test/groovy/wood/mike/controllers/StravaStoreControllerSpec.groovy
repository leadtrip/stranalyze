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
import wood.mike.domain.Activity
import wood.mike.domain.Athlete
import wood.mike.repositories.ActivityRepository
import wood.mike.repositories.AthleteRepository

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

    void "test fetchAthlete" () {
        when:
            client.toBlocking().exchange(HttpRequest.GET('/stravaStore/athlete'))
        then:
            stravaFetchClient.fetchAthlete() >> Mono.just( objectMapper.readValue(ATHLETE_STRING, Athlete) )
        and:
            def athleteFromDb = athleteRepository.find()
            athleteFromDb.isPresent()
            athleteFromDb.get().firstname == 'Chester'
    }

    void "test activity operations" () {
        given:
            readable.exists()
            List<Activity> mockActivities = activityList()
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
            def response = client.jsonStream( request, Activity ).collectList().block()
        then:
            response.size() == 5
    }

    def activityList() {
        objectMapper.readValue( readable.asInputStream(), Argument.listOf(Activity.class) )
    }

    @MockBean(StravaFetchClient)
    StravaFetchClient stravaFetchClient() {
        Mock(StravaFetchClient)
    }
}
