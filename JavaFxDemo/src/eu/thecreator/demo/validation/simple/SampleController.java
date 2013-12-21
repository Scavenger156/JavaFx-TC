/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.demo.validation.simple;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import eu.thecreator.validation.base.CustomValidator;
import eu.thecreator.validation.base.FXBaseValidator;
import eu.thecreator.validation.base.ValidationListener;
import eu.thecreator.validation.base.ValidationResult;
import eu.thecreator.validation.base.ValidationTyp;
import eu.thecreator.validation.base.Validationmessage;

/**
 * Demo für die Validatoren ohne Annontations.
 * 
 * @author Scavenger156
 * 
 */
public class SampleController implements Initializable, ValidationListener, CustomValidator {

	private static final String VALIDATION_MESSAGE = "Bitte geben sie \"Schön\" ein";
	private static final String TEXT_TO_CHECK = "Schön";

	@FXML
	private TextField textFieldNotNull;

	@FXML
	private TextField textFieldNotNullEmail;

	@FXML
	private TextField customValError;
	@FXML
	private TextField customValWarn;
	@FXML
	private TextField customValInfo;

	@FXML
	private ComboBox<String> comboBoxNotNull;

	@FXML
	private TextArea textAreaFieldNotNull;

	@FXML
	private TextArea textAreaFieldAllMessages;

	private FXBaseValidator fXValidator;

	/**
	 * 
	 * Konstruktor.
	 */
	public SampleController() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		comboBoxNotNull.getItems().add("1");
		comboBoxNotNull.getItems().add("2");
		comboBoxNotNull.getItems().add("3");
		comboBoxNotNull.getItems().add(null);

		// Validator erzeugen
		fXValidator = new eu.thecreator.validation.base.FXBaseValidator();

		fXValidator.setAdditionalValidator(this);
		fXValidator.setValidationListener(this);
		// Diese 3 Felder Validieren wir selber
		fXValidator.addCustomValidation(customValError);
		fXValidator.addCustomValidation(customValWarn);
		fXValidator.addCustomValidation(customValInfo);

		fXValidator.validate();

	}

	@Override
	public void validate(ValidationResult result) {
		// Eigene Validation durchführen
		String txt = customValError.getText();
		if (!TEXT_TO_CHECK.equals(txt)) {
			result.addViolation(customValError, VALIDATION_MESSAGE, ValidationTyp.ERROR);
		}
		txt = customValWarn.getText();
		if (!TEXT_TO_CHECK.equals(txt)) {
			result.addViolation(customValWarn, VALIDATION_MESSAGE, ValidationTyp.WARNING);
		}
		txt = customValInfo.getText();
		if (!TEXT_TO_CHECK.equals(txt)) {
			result.addViolation(customValInfo, VALIDATION_MESSAGE, ValidationTyp.INFO);
		}
	}

	@Override
	public void validated(ValidationResult result) {
		// Vir wollen drauf reagieren und Nachrichten ausgeben
		StringBuilder builder = new StringBuilder();
		for (Validationmessage validateHelper : result.getMessages()) {
			if (builder.length() > 0) {
				builder.append("\n");

			}
			builder.append(validateHelper.getValidationMessage());
		}
		textAreaFieldAllMessages.setText(builder.toString());
	}
}
