package eu.thecreator.validation.base;

import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.Property;
import javafx.scene.control.Control;

/**
 * Basisklasse des Validators mit Annontations. Validierung-Annontations werden
 * gesucht und Validierungsfehler auf der Oberfläche angezeigt.
 * 
 * @author Scavenger156
 * 
 */
public abstract class FxValidator extends FXBaseValidator {

	/**
	 * 
	 * Konstruktor.
	 */
	public FxValidator() {

	}

	/**
	 * Validiert ein Feld
	 * 
	 * @param toValidate
	 *            zu Validierendes Objekt
	 * @param result
	 *            Hier werden die Fehler gesammelt
	 */
	protected abstract void validate(ValidationmessageAnnontations toValidate, ValidationResult result);

	/**
	 * Validiert ein Feld
	 * 
	 * @param toValidate
	 *            zu Validierendes Objekt
	 * @param result
	 *            Hier werden die Fehler gesammelt
	 */
	@Override
	protected void validate(Validationmessage toValidate, ValidationResult result) {
		if (toValidate instanceof ValidationmessageAnnontations) {
			// Annontations weiterlassen
			validate((ValidationmessageAnnontations) toValidate, result);
		}
	}

	/**
	 * Liefert die Basisannontation die die Validierungsannontation haben muss.
	 * 
	 * @return Annontation der Validierungsannontation.
	 */
	protected abstract List<Class<? extends Annotation>> getValidationAnnontations();

	/**
	 * Objekt wird nach Annontationen durchsucht und die Listener für
	 * Validierungen hinzugefügt.
	 * 
	 * @param controller
	 *            Zu Analysierendes Objekt
	 */
	@SuppressWarnings("unchecked")
	public final void inspectObjectForValidation(final Object controller) {

		List<Class<? extends Annotation>> annontationsToCheck = getValidationAnnontations();

		for (Field field : controller.getClass().getDeclaredFields()) {

			Annotation[] annontations = field.getAnnotations();
			if (annontations != null) {
				List<Annotation> validationAnnontations = new ArrayList<>();
				for (Annotation annotation : annontations) {
					for (Class<? extends Annotation> toCheck : annontationsToCheck) {
						if (annotation.annotationType().getAnnotation(toCheck) != null) {
							// Validator gefunden
							validationAnnontations.add(annotation);
						}
					}

				}
				// Wir haben eine entsprechende Annontation gefunden
				if (!validationAnnontations.isEmpty()) {
					ValidationmessageAnnontations helper;
					Object value = ReflectionUtil.getFromField(field, controller);

					if (Property.class.isAssignableFrom(field.getType())) {
						// Propertyobject
						helper = new ValidationmessageAnnontations(controller, field, (Property<Object>) value, validationAnnontations);
						helper.fxProperty.addListener(this);
					} else if (Control.class.isAssignableFrom(field.getType())) {
						// FX Controllobjekt
						helper = new ValidationmessageAnnontations(controller, field, (Control) value, validationAnnontations);
						helper.fxProperty = installListenerOnControl(helper.fxElement);
					} else {
						// Normales Object
						helper = new ValidationmessageAnnontations(controller, field, validationAnnontations);
						// Suchen der Methode einen Listener hinzuzufügen
						ReflectionUtil.findAndInvokeMethod(controller, controller.getClass(), "addPropertyChangeListener",
								new Class[] { PropertyChangeListener.class }, new Object[] { this });

					}

					validateHelpers.add(helper);
				}

			}

		}
	}
}
