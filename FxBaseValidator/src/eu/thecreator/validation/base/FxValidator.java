package eu.thecreator.validation.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;

/**
 * Basisklasse des Validators. Validierung-Annontations werden gesucht und
 * Validierungsfehler auf der Oberfläche angezeigt.
 * 
 * @author Scavenger156
 * 
 */
public abstract class FxValidator implements InvalidationListener, PropertyChangeListener {
	private static final Map<Class<? extends Control>, String> CONTROLLHELPER = new HashMap<>();
	static {
		// Das ist etwa Magie da wir bei den einzelnen UIs wissen müssen
		// wogegen wir prüfen müssen
		CONTROLLHELPER.put(TextInputControl.class, "textProperty");
		// controllHelper.put(ComboBoxBase.class, "selectionModelProperty");
		CONTROLLHELPER.put(ComboBoxBase.class, "valueProperty");

	}

	/**
	 * Liste Aller Felder die Validiert werden
	 */
	private List<ValidationmessageImpl> validateHelpers = new ArrayList<>();
	private ValidationListener validationListener;
	private CustomValidator additionalValidator;

	/**
	 * 
	 * Konstruktor.
	 */
	public FxValidator() {

	}

	/**
	 * Wenn eigene UI-Elemente Validiert werden sollen kann in diese Map das
	 * Property hinzugefügt werden welches Validiert wird.
	 * 
	 * @return
	 */
	public static Map<Class<? extends Control>, String> getControllhelper() {
		return CONTROLLHELPER;
	}

	public void setValidationListener(ValidationListener validationListener) {
		this.validationListener = validationListener;
	}

	/**
	 * 
	 * @param additionalValidator
	 *            Eigener Valididator
	 */
	public void setAdditionalValidator(CustomValidator additionalValidator) {
		this.additionalValidator = additionalValidator;
	}

	/**
	 * Validiert ein Feld
	 * 
	 * @param toValidate
	 *            zu Validierendes Objekt
	 * @param result
	 *            Hier werden die Fehler gesammelt
	 */
	protected abstract void validate(Validationmessage toValidate, ValidationResult result);

	/**
	 * Hauptmethode die alles Validiert.
	 * 
	 * @return Ergebniss der Validierung.
	 */
	public ValidationResult validate() {
		clearValidation();
		ValidationResult result = new ValidationResult(this.validateHelpers);
		for (ValidationmessageImpl validateHelper : validateHelpers) {
			if (validateHelper.isCustomValidator()) {
				// Eigene Validatoren werden später mit dem
				// "additionalValidator" geprüft
				continue;
			}
			validate(validateHelper, result);

		}
		// Zusätzliche Validierung
		if (additionalValidator != null) {
			ValidationResult validationResultCustom = new ValidationResult(this.validateHelpers);
			additionalValidator.validate(validationResultCustom);

			result.getMessages().addAll(validationResultCustom.getMessages());
		}

		provideFeedbackForUI(result);
		if (validationListener != null) {
			validationListener.validated(result);
		}
		return result;
	}

	/**
	 * Hintergundfarbe für Fehler setzen
	 * 
	 * @param result
	 *            Ergebniss der Validierung
	 */
	private void provideFeedbackForUI(ValidationResult result) {
		for (Validationmessage validateHelper : result.getMessages()) {

			if (validateHelper.fxElement != null && validateHelper.validationMessage != null) {
				if (validateHelper.validationTyp == ValidationTyp.ERROR) {
					validateHelper.fxElement.setStyle("-fx-border-color: red");
				}
				if (validateHelper.validationTyp == ValidationTyp.WARNING) {
					validateHelper.fxElement.setStyle("-fx-border-color: yellow");
				}

			}

		}
	}

	/**
	 * Validierungensmeldungen entfernen
	 */
	private void clearValidation() {

		for (Validationmessage validateHelper : validateHelpers) {
			validateHelper.validationMessage.set(null);
			if (validateHelper.fxElement != null) {
				validateHelper.fxElement.setStyle("-fx-border-color: null");
			}

		}
	}

	/**
	 * Wir müssen immer an das korrekte Property des Controls unseren Listener
	 * hÃ¤ngen.
	 * 
	 * @param control
	 *            Control dessen Property wir suchen
	 * @return das Property des Controls das für die Validierung wichtig ist
	 */
	@SuppressWarnings("unchecked")
	private Property<?> installListenerOnControl(Control control) {
		Property<?> p = null;
		Class<? extends Control> current = control.getClass();
		String txtProperty = null;
		while (!current.isAssignableFrom(Object.class)) {
			txtProperty = CONTROLLHELPER.get(current);
			if (txtProperty != null) {
				break;
			}
			current = (Class<? extends Control>) current.getSuperclass();
		}
		if (txtProperty != null) {
			p = (Property<?>) ReflectionUtil.findAndInvokeMethod(control, current, txtProperty, null, null);
		}
		if (p != null) {
			p.addListener(this);
		}
		return p;
	}

	/**
	 * Feld Validieren aber das übernimmt der User. Listener werden hinzugefügt
	 * um auf Änderungen zu reagieren
	 * 
	 * @param customVal
	 *            Dieses Controll validiert der User selber
	 */
	public final void addCustomValidation(Control customVal) {

		// FX Object
		ValidationmessageImpl helper = new ValidationmessageImpl(customVal);
		helper.fxProperty = installListenerOnControl(helper.fxElement);
		validateHelpers.add(helper);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		validate();

	}

	@Override
	public void invalidated(Observable arg0) {
		validate();
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
				boolean install = false;

				for (Annotation annotation : annontations) {
					for (Class<? extends Annotation> toCheck : annontationsToCheck) {
						if (annotation.annotationType().getAnnotation(toCheck) != null) {
							// Validator gefunden
							install = true;
							break;
						}
					}
					if (install) {
						break;
					}

				}
				// Wir haben eine entsprechende Annontation gefunden
				if (install) {
					ValidationmessageImpl helper;
					Object value = ReflectionUtil.getFromField(field, controller);

					if (Property.class.isAssignableFrom(field.getType())) {
						// Propertyobject
						helper = new ValidationmessageImpl(controller, field, (Property<Object>) value);
						helper.fxProperty.addListener(this);
					} else if (Control.class.isAssignableFrom(field.getType())) {
						// FX Controllobjekt
						helper = new ValidationmessageImpl(controller, field, (Control) value);
						helper.fxProperty = installListenerOnControl(helper.fxElement);
					} else {
						// Normales Object
						helper = new ValidationmessageImpl(controller, field);
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
