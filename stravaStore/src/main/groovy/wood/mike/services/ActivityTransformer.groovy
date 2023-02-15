package wood.mike.services

import wood.mike.ActivityDto
import wood.mike.domain.ActivityEntity

/**
 * Convert activity DTO to entity and vice versa
 */
class ActivityTransformer {

    static ActivityDto from( ActivityEntity activityEntity ) {
        ActivityDto activityDto = new ActivityDto()
        activityDto.id = activityEntity.id
        activityDto.name = activityEntity.name
        activityDto.distance = activityEntity.distance
        activityDto.moving_time = activityEntity.moving_time
        activityDto.elapsed_time = activityEntity.elapsed_time
        activityDto.total_elevation_gain = activityEntity.total_elevation_gain
        activityDto.sport_type = activityEntity.sportType
        activityDto.start_date = activityEntity.startDate
        activityDto.timezone = activityEntity.timezone
        activityDto.average_speed = activityEntity.average_speed
        activityDto.max_speed = activityEntity.max_speed
        activityDto.average_cadence = activityEntity.average_cadence
        activityDto.average_watts = activityEntity.average_watts
        activityDto.weighted_average_watts = activityEntity.weighted_average_watts
        activityDto.kilojoules = activityEntity.kilojoules
        activityDto.average_heartrate = activityEntity.average_heartrate
        activityDto.max_heartrate = activityEntity.max_heartrate
        activityDto.max_watts = activityEntity.max_watts
        activityDto.suffer_score = activityEntity.suffer_score
        activityDto
    }

    static ActivityEntity to( ActivityDto activityDto ) {
        ActivityEntity activityEntity = new ActivityEntity()
        activityEntity.id = activityDto.id
        activityEntity.name = activityDto.name
        activityEntity.distance = activityDto.distance
        activityEntity.moving_time = activityDto.moving_time
        activityEntity.elapsed_time = activityDto.elapsed_time
        activityEntity.total_elevation_gain = activityDto.total_elevation_gain
        activityEntity.sportType = activityDto.sport_type
        activityEntity.startDate = activityDto.start_date
        activityEntity.timezone = activityDto.timezone
        activityEntity.average_speed = activityDto.average_speed
        activityEntity.max_speed = activityDto.max_speed
        activityEntity.average_cadence = activityDto.average_cadence
        activityEntity.average_watts = activityDto.average_watts
        activityEntity.weighted_average_watts = activityDto.weighted_average_watts
        activityEntity.kilojoules = activityDto.kilojoules
        activityEntity.average_heartrate = activityDto.average_heartrate
        activityEntity.max_heartrate = activityDto.max_heartrate
        activityEntity.max_watts = activityDto.max_watts
        activityEntity.suffer_score = activityDto.suffer_score
        activityEntity
    }
}
