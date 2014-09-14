package eu.thecreator.validation.tc.annontation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Validator
@Target({ FIELD })
@Retention(RUNTIME)
public @interface NotNull {
	/**
	 * Pr�ft bei einem String auch ob �berhaupt etwas eingeben wurde
	 * 
	 * @return Pr�ft bei einem String auch ob �berhaupt etwas eingeben wurde
	 */
	boolean checkIsEmpty() default true;
}
