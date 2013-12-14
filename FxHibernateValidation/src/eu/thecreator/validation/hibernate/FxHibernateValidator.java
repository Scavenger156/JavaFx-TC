package eu.thecreator.validation.hibernate;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.control.SelectionModel;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import eu.thecreator.validation.base.FxValidator;
import eu.thecreator.validation.base.ValidationResult;
import eu.thecreator.validation.base.ValidationmessageImpl;

public class FxHibernateValidator extends FxValidator {
	private Validator validator;

	public FxHibernateValidator() {
		ValidatorFactory fact = Validation.buildDefaultValidatorFactory();
		validator = fact.getValidator();
	}

	@Override
	protected void validate(ValidationmessageImpl toValidate, ValidationResult result) {
		// validieren des Objektes durchführen
		Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
		// Unterschied ob ein Property, ein FX Element oder ein Textvo
		if (toValidate.getFxProperty() != null) {
			Object value = toValidate.getFxProperty().getValue();
			if (value instanceof String) {
				if (value.toString().trim().length() == 0) {
					value = null;
				}
			} else if (value instanceof SelectionModel) {
				value = ((SelectionModel<?>) value).getSelectedItem();
			}
			Set<?> validationMessages = validator.validateValue(toValidate.getController().getClass(), toValidate.getField().getName(), value, new Class[0]);

			constraintViolations.addAll((Collection<? extends ConstraintViolation<?>>) validationMessages);
		} else if (toValidate.getController() != null) {
			// Kann eigentlich nicht vorkommen da wir immer ein Porperty
			// haben
		} else {
			Set<ConstraintViolation<Object>> validationMessages = validator.validateProperty(toValidate.getController(), toValidate.getField().getName(),
					Default.class);
			constraintViolations.addAll(validationMessages);
		}
		if (!constraintViolations.isEmpty()) {
			StringBuilder buffer = new StringBuilder();
			for (ConstraintViolation<?> vioalation : constraintViolations) {
				buffer.append(vioalation.getMessage());

			}
			toValidate.getProertyValidationMessage().set(buffer.toString());
			result.getMessages().add(toValidate);
		}
	}

	@Override
	protected List<Class<? extends Annotation>> getValidationAnnontations() {
		List<Class<? extends Annotation>> tmp = new ArrayList<>();
		tmp.add(Constraint.class);
		return tmp;
	}
}
