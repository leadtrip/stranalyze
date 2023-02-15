package wood.mike.repositories

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import wood.mike.domain.AthleteEntity

@JdbcRepository(dialect = Dialect.MYSQL)
interface AthleteRepository extends CrudRepository<AthleteEntity, Long> {
    AthleteEntity persist(AthleteEntity athlete)

    Optional<AthleteEntity> find()
}