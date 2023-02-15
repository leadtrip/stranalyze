package wood.mike.services

import wood.mike.AthleteDto
import wood.mike.domain.AthleteEntity

/**
 * Convert athlete DTO to entity and vice versa
 */
class AthleteTransformer {

    static AthleteDto from( AthleteEntity athleteEntity ) {
        AthleteDto athleteDto = new AthleteDto()
        athleteDto.id = athleteEntity.id
        athleteDto.firstname = athleteEntity.firstname
        athleteDto.lastname = athleteEntity.lastname
        athleteDto.city = athleteEntity.city
        athleteDto.state = athleteEntity.state
        athleteDto.country = athleteEntity.country
        athleteDto.sex = athleteEntity.sex
        athleteDto
    }

    static AthleteEntity to ( AthleteDto athleteDto ) {
        AthleteEntity athleteEntity = new AthleteEntity()
        athleteEntity.id = athleteDto.id
        athleteEntity.firstname = athleteDto.firstname
        athleteEntity.lastname = athleteDto.lastname
        athleteEntity.city = athleteDto.city
        athleteEntity.state = athleteDto.state
        athleteEntity.country = athleteDto.country
        athleteEntity.sex = athleteDto.sex
        athleteEntity
    }
}
