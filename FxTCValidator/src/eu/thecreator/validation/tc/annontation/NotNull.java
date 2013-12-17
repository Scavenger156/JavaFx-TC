package eu.thecreator.validation.tc.annontation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Validator
@Target({ FIELD })
@Retention(RUNTIME)
public @interface NotNull {

}
