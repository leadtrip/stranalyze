package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
class AthleteDto {
    Long id
    String firstname
    String lastname
    String city
    String state
    String country
    String sex
}
