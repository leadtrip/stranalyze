package wood.mike.annotations

import io.micronaut.http.annotation.FilterMatcher

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker interface used to determine whether authentication should be used when calling Strava API
 */
@FilterMatcher
@Documented
@Retention(RUNTIME)
@Target([TYPE, PARAMETER])
@interface StravaAuth {

}