package wood.mike

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import wood.mike.clients.StravaFetchClient
import wood.mike.domain.Athlete
import wood.mike.repositories.AthleteRepository


@MicronautTest
class StravaFetchControllerSpec extends Specification{

    static final String ATHLETE_STRING =
            '''{
                "id": 2313212,
                "firstname": "Mike",
                "lastname": "Wood",
                "city": "Melksham",
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
    HttpClient client

    @Inject
    AthleteRepository athleteRepository

    void "test fetchAthlete" () {
        when:
            client.toBlocking().exchange(HttpRequest.GET('/stravaStore/athlete'))
        then:
            stravaFetchClient.fetchAthlete() >> objectMapper.readValue(ATHLETE_STRING, Athlete)
    }

    @MockBean(StravaFetchClient)
    StravaFetchClient stravaFetchClient() {
        Mock(StravaFetchClient)
    }
}
