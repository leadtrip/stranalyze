package wood.mike.repositories

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import wood.mike.domain.Athlete

@JdbcRepository(dialect = Dialect.MYSQL)
interface AthleteRepository extends CrudRepository<Athlete, Long> {
    Athlete persist(Athlete athlete)

    Optional<Athlete> find()
}