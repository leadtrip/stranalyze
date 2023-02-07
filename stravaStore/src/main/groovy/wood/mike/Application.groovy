package wood.mike

import io.micronaut.context.env.Environment
import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
        info = @Info(
                title = "StravaStore",
                description = 'Stores data retrieved from Strava by the StravaFetch service',
                version = "0.1"
        )
)
class Application {

    static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application.class)
                .defaultEnvironments(Environment.DEVELOPMENT)
                .start();
    }
}
