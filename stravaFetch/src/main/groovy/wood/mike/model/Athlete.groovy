package wood.mike.model

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
class Athlete {
    Long id
    String firstname
    String lastname
    String city
    String state
    String country
    String sex
}
