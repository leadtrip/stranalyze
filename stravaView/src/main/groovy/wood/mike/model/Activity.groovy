package wood.mike.model

import groovy.transform.CompileStatic
import io.micronaut.core.convert.format.Format
import io.micronaut.serde.annotation.Serdeable
import org.thymeleaf.util.DateUtils

import java.time.LocalDateTime
import java.time.LocalTime

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

    def elevationGainMetersAndFeet() {
        "$total_elevation_gain (${(total_elevation_gain * 3.281).round()})"
    }
}
