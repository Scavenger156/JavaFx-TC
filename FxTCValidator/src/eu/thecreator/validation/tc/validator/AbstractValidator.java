package eu.thecreator.validation.tc.validator;

public abstract class AbstractValidator<T> {
	private Class<?> validatorClass;

	public AbstractValidator() {
		// TODO Per Reflection Type t = getClass().getGenericSuperclass();
		validatorClass = getValidationTyp();
	}

	protected abstract Class<?> getValidationTyp();

	public abstract boolean isValid(T value);

	public boolean acceptObjectType(Object value) {
		if (value == null) {
			return true;
		}
		return validatorClass.isInstance(value);
	}

	public abstract String getValidationMessage();
}
