package eu.thecreator.validation.base;

public class Violation {
	private String message;
	private ValidationTyp validationTyp = ValidationTyp.ERROR;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ValidationTyp getValidationTyp() {
		return validationTyp;
	}

	public Violation(String message, ValidationTyp validationTyp) {
		super();
		this.message = message;
		this.validationTyp = validationTyp;
	}

	public void setValidationTyp(ValidationTyp validationTyp) {
		this.validationTyp = validationTyp;
	}

	public Violation(String message) {
		super();
		this.message = message;
	}

}
