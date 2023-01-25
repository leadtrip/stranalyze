package wood.mike.repositories

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import wood.mike.domain.Activity

import java.time.LocalDateTime

@JdbcRepository(dialect = Dialect.MYSQL)
interface ActivityRepository extends CrudRepository<Activity, Long> {
    Activity persist(Activity activity)

    @Query('select * from activity where strava_activity_id = :activityId' )
    Optional<Activity> findByStravaActivityId(Long activityId)

    List<Activity> findAllByStartDateAfter( LocalDateTime after )

    List<Activity> findAllBySportTypeIlike( String type )
}
