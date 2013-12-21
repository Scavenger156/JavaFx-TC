package eu.thecreator.validation.tc.validator;

import java.lang.annotation.Annotation;

public abstract class AbstractValidator<T, A extends Annotation> {
	private Class<?> validatorClass;

	public AbstractValidator() {
		// TODO Per Reflection Type t = getClass().getGenericSuperclass();
		validatorClass = getValidationTyp();
	}

	protected abstract Class<?> getValidationTyp();

	public abstract boolean isValid(T value, A annontation);

	public boolean acceptObjectType(Object value) {
		if (value == null) {
			return true;
		}
		return validatorClass.isInstance(value);
	}

	public abstract String getValidationMessage();
}
