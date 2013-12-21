package eu.thecreator.validation.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
 * Basisvalidator welcher nur einen eigenen Validator �ber "additionalValidator"
 * unterst�tzt. Stellt Nachrichten �ber die Oberfl�che dar mittels zus�tzlichen
 * Icons.
 * 
 * @author Scavenger156
 * 
 */
public class FXBaseValidator implements InvalidationListener, PropertyChangeListener {
	private static final Map<Class<? extends Control>, String> CONTROLLHELPER = new HashMap<>();
	static {
		// Das ist etwa Magie da wir bei den einzelnen UIs wissen m�ssen
		// wogegen wir pr�fen m�ssen
		CONTROLLHELPER.put(TextInputControl.class, "textProperty");
		// controllHelper.put(ComboBoxBase.class, "selectionModelProperty");
		CONTROLLHELPER.put(ComboBoxBase.class, "valueProperty");

	}

	/**
	 * Liste Aller Felder die Validiert werden
	 */
	protected List<ValidationmessageImpl> validateHelpers = new ArrayList<>();
	private ValidationListener validationListener;
	private CustomValidator additionalValidator;

	/**
	 * 
	 * Konstruktor.
	 */
	public FXBaseValidator() {

	}

	/**
	 * Wenn eigene UI-Elemente Validiert werden sollen kann in diese Map das
	 * Property hinzugef�gt werden welches Validiert wird.
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
	 * Hintergundfarbe f�r Fehler setzen
	 * 
	 * @param result
	 *            Ergebniss der Validierung
	 */
	protected void provideFeedbackForUI(ValidationResult result) {
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
	protected void clearValidation() {

		for (Validationmessage validateHelper : validateHelpers) {
			validateHelper.validationMessage.set(null);
			if (validateHelper.fxElement != null) {
				validateHelper.fxElement.setStyle("-fx-border-color: null");
			}

		}
	}

	/**
	 * Feld Validieren aber das �bernimmt der User. Listener werden hinzugef�gt
	 * um auf �nderungen zu reagieren
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
	 * Validiert ein Feld
	 * 
	 * @param toValidate
	 *            zu Validierendes Objekt
	 * @param result
	 *            Hier werden die Fehler gesammelt
	 */
	protected void validate(Validationmessage toValidate, ValidationResult result) {
		// Leer da hier der Annontation Code kommen w�rde
	}

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
				// Eigene Validatoren werden sp�ter mit dem
				// "additionalValidator" gepr�ft
				continue;
			}
			validate(validateHelper, result);

		}
		// Zus�tzliche Validierung
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
	 * Wir m�ssen immer an das korrekte Property des Controls unseren Listener
	 * hängen.
	 * 
	 * @param control
	 *            Control dessen Property wir suchen
	 * @return das Property des Controls das f�r die Validierung wichtig ist
	 */
	@SuppressWarnings("unchecked")
	protected Property<?> installListenerOnControl(Control control) {
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

}
