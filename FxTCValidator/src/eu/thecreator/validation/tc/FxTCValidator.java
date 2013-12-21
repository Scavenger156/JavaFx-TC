package eu.thecreator.validation.tc;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.SelectionModel;
import eu.thecreator.validation.base.ReflectionUtil;
import eu.thecreator.validation.base.ValidationResult;
import eu.thecreator.validation.base.ValidationmessageAnnontations;
import eu.thecreator.validation.tc.annontation.Email;
import eu.thecreator.validation.tc.annontation.NotNull;
import eu.thecreator.validation.tc.annontation.Validator;
import eu.thecreator.validation.tc.validator.AbstractValidator;
import eu.thecreator.validation.tc.validator.EmailValidator;
import eu.thecreator.validation.tc.validator.NotNullValdiator;

public class FxTCValidator extends eu.thecreator.validation.base.FxValidator {
	private Map<Class<? extends Annotation>, AbstractValidator<?, ?>> validatoren = new HashMap<>();

	protected void registerDefaultValidatoren() {
		validatoren.put(NotNull.class, new NotNullValdiator());
		validatoren.put(Email.class, new EmailValidator());
	}

	public FxTCValidator() {
		registerDefaultValidatoren();
	}

	@Override
	protected void validate(ValidationmessageAnnontations toValidate, ValidationResult result) {
		List<Annotation> annontations = toValidate.getValidationAnnontations();
		Set<String> errors = new HashSet<>();
		for (Annotation annotation : annontations) {
			if (annotation.annotationType().getAnnotation(Validator.class) != null) {
				@SuppressWarnings("unchecked")
				AbstractValidator<Object, Annotation> validator = (AbstractValidator<Object, Annotation>) validatoren.get(annotation.annotationType());
				if (validator == null) {
					throw new IllegalArgumentException("Validator nicht gefunden:" + annotation.annotationType());
				}
				Object value = null;

				if (toValidate.getFxProperty() != null) {
					value = toValidate.getFxProperty().getValue();
					if (value instanceof String) {
						if (((String) value).length() == 0) {
							value = null;
						}
					} else if (value instanceof SelectionModel) {
						value = ((SelectionModel<?>) value).getSelectedItem();
					}

				} else {
					value = ReflectionUtil.getFromField(toValidate.getField(), toValidate.getController());
				}
				if (validator.acceptObjectType(value) && !validator.isValid(value, annotation)) {
					errors.add(validator.getValidationMessage());
				}

			}
		}
		if (!errors.isEmpty()) {
			StringBuilder buffer = new StringBuilder();
			for (String vioalation : errors) {
				buffer.append(vioalation);

			}
			toValidate.getProertyValidationMessage().set(buffer.toString());
			result.getMessages().add(toValidate);
		}
	}

	@Override
	protected List<Class<? extends Annotation>> getValidationAnnontations() {
		List<Class<? extends Annotation>> tmp = new ArrayList<>();
		tmp.add(Validator.class);
		return tmp;
	}

}
