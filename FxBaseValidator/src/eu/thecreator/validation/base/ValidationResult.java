package eu.thecreator.validation.base;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Control;

public class ValidationResult {
	private List<Validationmessage> helpers = new ArrayList<>();

	private List<? extends Validationmessage> allValidators;

	public ValidationResult(List<? extends Validationmessage> allValidators) {
		super();
		this.allValidators = allValidators;
	}

	public void addViolation(Control fxElement, String msg) {
		addViolation(fxElement, msg, ValidationTyp.ERROR);
	}

	/**
	 * Einen neuen Fehler hinzufügen
	 * 
	 * @param fxElement
	 *            Element welches Fehlerhaft ist
	 * @param msg
	 *            Meldung
	 * @param validationTyp
	 */
	public void addViolation(Control fxElement, String msg, ValidationTyp validationTyp) {
		for (Validationmessage validateHelper : allValidators) {
			if (validateHelper.fxElement == fxElement) {
				validateHelper.validationMessage.setValue(msg);
				validateHelper.setValidationTyp(validationTyp);
				helpers.add(validateHelper);
				break;
			}
		}

	}

	public boolean hasErros() {
		for (Validationmessage toCheck : helpers) {
			if (toCheck.validationTyp == ValidationTyp.ERROR) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @return Die Aktuellen Fehlernachrichten
	 */
	public List<Validationmessage> getMessages() {
		return helpers;
	}
}
