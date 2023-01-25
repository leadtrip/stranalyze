package wood.mike.domain

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import io.micronaut.core.convert.format.Format
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.serde.annotation.Serdeable

import java.time.LocalDateTime

@Serdeable
@CompileStatic
@MappedEntity
class Activity {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    @MappedProperty( value = 'activity_id' )
    Long activityId
    @MappedProperty( value = 'strava_activity_id')
    Long id
    //@JsonProperty("athleteid")
    //@MappedProperty( converterPersistedType = Long, value = 'athlete_id' )
    Athlete athlete
    String name
    Long distance
    Long moving_time
    Long elapsed_time
    Long total_elevation_gain

    @MappedProperty( value = 'sport_type')
    @JsonProperty("sport_type")
    String sportType

    @MappedProperty( value = 'start_date')
    @Format("yyyy-MM-ddThh:mm:ssZ")
    @JsonProperty("start_date")
    LocalDateTime startDate

    String timezone
    Double average_speed
    Integer max_speed
    Double average_cadence
    Double average_watts
    Integer weighted_average_watts
    Double kilojoules
    Double average_heartrate
    Integer max_heartrate
    Integer max_watts
    Integer suffer_score
}
