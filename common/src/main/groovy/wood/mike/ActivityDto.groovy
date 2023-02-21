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

    def elapsedToHoursMinutesSeconds() {
        toHoursMinutesSeconds(elapsed_time)
    }

    def movingToHoursMinutesSeconds() {
        toHoursMinutesSeconds(moving_time)
    }

    def toHoursMinutesSeconds( Long field ) {
        def hours = field / 3600
        def minutes = (field % 3600) / 60
        def seconds = field % 60
        String.format("%02d:%02d:%02d", hours.longValue(), minutes.longValue(), seconds.longValue())
    }

    def distanceKm() {
        (distance * 0.001).round()
    }
}
