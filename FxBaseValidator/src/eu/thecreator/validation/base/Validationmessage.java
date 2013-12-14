package eu.thecreator.validation.base;

import java.lang.reflect.Field;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;

public class Validationmessage {
	protected final StringProperty validationMessage = new SimpleStringProperty();
	protected Control fxElement;
	protected Property<?> fxProperty;
	protected ValidationTyp validationTyp = ValidationTyp.ERROR;
	protected Object controller;
	protected Field field;

	public Validationmessage() {

	}

	public String getValidationMessage() {
		return validationMessage.get();
	}

	public StringProperty getProertyValidationMessage() {
		return validationMessage;
	}

	public Field getField() {
		return field;
	}

	public Object getController() {
		return controller;
	}

	public void setValidationTyp(ValidationTyp validationTyp) {
		this.validationTyp = validationTyp;
	}

	public Property<?> getFxProperty() {
		return fxProperty;
	}

	public Control getFxElement() {
		return fxElement;
	}

}
