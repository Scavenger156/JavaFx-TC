package eu.thecreator.validation.tc.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends AbstractValidator<String> {
	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	@Override
	protected Class<?> getValidationTyp() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public boolean isValid(String value) {
		if (value == null) {
			return false;
		}
		matcher = pattern.matcher(value);
		return matcher.matches();
	}

	@Override
	public String getValidationMessage() {
		return "Der Wert entspricht keiner Gültigen Emailadresse";
	}

}
