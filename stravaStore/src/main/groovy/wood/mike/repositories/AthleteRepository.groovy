package wood.mike.repositories


import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.CrudRepository
import wood.mike.domain.AthleteEntity

@R2dbcRepository(dialect = Dialect.MYSQL)
interface AthleteRepository extends CrudRepository<AthleteEntity, Long> {
    AthleteEntity persist(AthleteEntity athlete)

    Optional<AthleteEntity> find()
}