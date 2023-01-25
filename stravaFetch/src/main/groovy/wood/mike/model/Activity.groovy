package wood.mike.model

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
class Activity {
    Long id
    Athlete athlete
    String name
    Long distance
    Long moving_time
    Long elapsed_time
    Long total_elevation_gain
    String sport_type
    String start_date
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
