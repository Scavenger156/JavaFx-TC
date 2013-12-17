package eu.thecreator.validation.tc.validator;

public class NotNullValdiator extends AbstractValidator<Object> {

	@Override
	public boolean isValid(Object value) {
		if (value == null) {
			return false;
		} else if (value instanceof String) {
			String str = (String) value;
			return str.trim().length() > 0;
		}
		return true;
	}

	@Override
	protected Class<?> getValidationTyp() {

		return Object.class;
	}

	@Override
	public String getValidationMessage() {
		return "Der Wert darf nicht leer sein";
	}

}
