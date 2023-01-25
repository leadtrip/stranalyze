package wood.mike.domain

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
@MappedEntity
class Athlete {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    Long appId
    @MappedProperty( value = 'strava_athlete_id')
    Long id
    String firstname
    String lastname
    String city
    String state
    String country
    String sex
}
