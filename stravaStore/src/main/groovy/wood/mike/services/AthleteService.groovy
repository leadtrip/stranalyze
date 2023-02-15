package wood.mike.services

import jakarta.inject.Singleton
import reactor.core.publisher.Mono
import wood.mike.AthleteDto
import wood.mike.clients.StravaFetchClient
import wood.mike.domain.AthleteEntity
import wood.mike.repositories.AthleteRepository

@Singleton
class AthleteService {

    private final AthleteRepository athleteRepository
    private final StravaFetchClient stravaFetchClient

    AthleteService(AthleteRepository athleteRepository, StravaFetchClient stravaFetchClient) {
        this.athleteRepository = athleteRepository
        this.stravaFetchClient = stravaFetchClient
    }

    Mono<AthleteDto> fetchAthlete() {
        Optional<AthleteEntity> athlete = athleteRepository.find()
        if( athlete.isPresent() ) {
            return Mono<AthleteDto>.just(AthleteTransformer.from(athlete.get()))
        }
        stravaFetchClient.fetchAthlete()
                .map(AthleteTransformer::to)
                .map(athleteRepository::persist)
                .map (AthleteTransformer::from)
    }
}
