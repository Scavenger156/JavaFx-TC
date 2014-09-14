package eu.thecreator.validation.base;

import java.lang.reflect.Field;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;

/**
 * Basisimplementierung der Validerungsnachrichten so das diese auf der
 * Oberfl�che erscheinen k�nnen
 * 
 * @author Scavenger156
 * 
 */
public class Validationmessage {
	protected final StringProperty validationMessage = new SimpleStringProperty();
	protected Control fxElement;
	protected Property<?> fxProperty;
	protected ValidationTyp validationTyp = ValidationTyp.ERROR;
	protected Object controller;
	protected Field field;

	public Validationmessage() {

	}

	/**
	 * 
	 * @return Text der Validierung
	 */
	public String getValidationMessage() {
		return validationMessage.get();
	}

	/**
	 * 
	 * @return Property der Validierung
	 */
	public StringProperty getProertyValidationMessage() {
		return validationMessage;
	}

	/**
	 * 
	 * @return Reflectionfeld
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 
	 * @return Controller der das Feld h�lt welches Validiert wird
	 */
	public Object getController() {
		return controller;
	}

	/**
	 * �ndern des Validierungstypes
	 * 
	 * @param validationTyp
	 *            neuer Typ
	 */
	public void setValidationTyp(ValidationTyp validationTyp) {
		this.validationTyp = validationTyp;
	}

	/**
	 * 
	 * @return Property welches beim Validiern gepr�ft wird
	 */
	public Property<?> getFxProperty() {
		return fxProperty;
	}

}
