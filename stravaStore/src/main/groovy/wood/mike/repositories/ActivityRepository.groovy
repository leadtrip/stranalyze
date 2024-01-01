package wood.mike.repositories

import io.micronaut.data.annotation.Query
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import wood.mike.domain.ActivityEntity

import java.time.LocalDateTime

@R2dbcRepository(dialect = Dialect.MYSQL)
interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {
    ActivityEntity persist(ActivityEntity activity)

    @Query('select * from activity where strava_activity_id = :activityId' )
    Optional<ActivityEntity> findByStravaActivityId(Long activityId)

    List<ActivityEntity> findAllByStartDateAfter(LocalDateTime after )

    List<ActivityEntity> findAllBySportTypeIlike(String type )

    Optional<Long> findMaxId()

    Optional<LocalDateTime> findMaxStartDate()

    @Query('select * from activity where strava_activity_id = (select max(strava_activity_id) from activity)')
    Optional<ActivityEntity> findLatestActivity()
}
