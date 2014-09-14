/**
 * 
 */
package eu.thecreator.validation.tc.annontation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Scavenger156
 * 
 */
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Validator {

}
