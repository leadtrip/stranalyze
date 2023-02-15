package wood.mike.domain

import io.micronaut.serde.annotation.Serdeable

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serdeable
class ActivitySummary {

    Long id
    String name
    String startDate
    String duration

    ActivitySummary(ActivityEntity activity ) {
        this.id = activity.id
        this.name = activity.name
        this.startDate = activity.startDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
        this.duration = elapsedToHoursMinutesSeconds(activity.elapsed_time)
    }

    static def elapsedToHoursMinutesSeconds(Long elapsed ) {
        def hours = elapsed / 3600
        def minutes = (elapsed % 3600) / 60
        def seconds = elapsed % 60
        String.format("%02d:%02d:%02d", hours.longValue(), minutes.longValue(), seconds.longValue())
    }
}
