package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.core.convert.format.Format
import io.micronaut.serde.annotation.Serdeable

import java.time.LocalDateTime

@Serdeable
@CompileStatic
class ActivityDto {
    Long id
    AthleteDto athlete
    String name
    Long distance
    Long moving_time
    Long elapsed_time
    Long total_elevation_gain
    String sport_type

    @Format("yyyy-MM-ddThh:mm:ssZ")
    LocalDateTime start_date
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
