package eu.thecreator.validation.tc.validator;

import eu.thecreator.validation.tc.annontation.NotNull;

/**
 * Implementation des Not Null Validators
 * 
 * @author Scavenger156
 * 
 */
public class NotNullValdiator extends AbstractValidator<Object, NotNull> {
	/**
	 * 
	 * Konstruktor.
	 */
	public NotNullValdiator() {

	}

	@Override
	public boolean isValid(Object value, NotNull annontation) {
		if (value == null) {
			return false;
		} else if (annontation.checkIsEmpty() && value instanceof String) {
			String str = (String) value;
			return str.trim().length() > 0;
		}
		return true;
	}

	@Override
	protected Class<?> getValidationTyp() {
		// Gilt für Alle Objekte
		return Object.class;
	}

	@Override
	public String getValidationMessage() {
		return "Der Wert darf nicht leer sein";
	}

}
