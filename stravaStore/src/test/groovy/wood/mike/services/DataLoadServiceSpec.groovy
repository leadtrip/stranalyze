package wood.mike.services

import io.micronaut.context.annotation.Value
import io.micronaut.core.io.Readable
import io.micronaut.core.type.Argument
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import spock.lang.Specification
import wood.mike.clients.StravaFetchClient
import wood.mike.domain.ActivityEntity
import wood.mike.repositories.ActivityRepository

@MicronautTest
class DataLoadServiceSpec extends Specification {

    @Inject
    ObjectMapper objectMapper

    @Inject
    StravaFetchClient stravaFetchClient

    @Inject
    DataLoadService dataLoadService

    @Inject
    ActivityRepository activityRepository

    @Value("classpath:testactivities.json")
    Readable readable

    void "test loadLatestActivites"() {
        given:
            def aList =  objectMapper.readValue( readable.asInputStream(), Argument.listOf(ActivityEntity.class) )
        when:
            dataLoadService.loadLatestActivites()
        then:
            stravaFetchClient.activitiesAfter(_) >> Flux.fromIterable( aList )
        then:
            activityRepository.findAll().size() == 10
    }

    @MockBean(StravaFetchClient)
    StravaFetchClient stravaFetchClient() {
        Mock(StravaFetchClient)
    }
}
