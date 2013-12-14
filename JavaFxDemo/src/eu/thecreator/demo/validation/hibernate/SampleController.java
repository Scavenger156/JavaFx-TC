/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.demo.validation.hibernate;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import eu.thecreator.validation.base.CustomValidator;
import eu.thecreator.validation.base.FxValidator;
import eu.thecreator.validation.base.ValidationListener;
import eu.thecreator.validation.base.ValidationResult;
import eu.thecreator.validation.base.ValidationTyp;
import eu.thecreator.validation.base.Validationmessage;
import eu.thecreator.validation.hibernate.FxHibernateValidator;

/**
 * 
 * @author andre
 */
public class SampleController implements Initializable, ValidationListener, CustomValidator {

	@NotNull
	@FXML
	TextField textFieldNotNull;
	@NotNull
	@Email
	@FXML
	TextField textFieldNotNullEmail;

	@FXML
	TextField customValError;
	@FXML
	TextField customValWarn;
	@FXML
	TextField customValInfo;

	@NotNull
	@FXML
	ComboBox<String> comboBoxNotNull;
	@NotNull
	@FXML
	TextArea textAreaFieldNotNull;

	@FXML
	TextArea textAreaFieldAllMessages;

	private FxValidator fXValidator;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		comboBoxNotNull.getItems().add("1");
		comboBoxNotNull.getItems().add("2");
		comboBoxNotNull.getItems().add("3");
		comboBoxNotNull.getItems().add(null);

		fXValidator = new FxHibernateValidator();
		// Unseren Controller anmelden
		fXValidator.inspectObject(this);

		fXValidator.setAdditionalValidator(this);
		fXValidator.setValidationListener(this);
		fXValidator.addCustomValidation(customValError);
		fXValidator.addCustomValidation(customValWarn);
		fXValidator.addCustomValidation(customValInfo);

		fXValidator.validate();

	}

	@Override
	public void validate(ValidationResult result) {
		// Eigener Validator
		String txt = customValError.getText();
		if (!"Schön".equals(txt)) {
			result.addViolation(customValError, "Bitte geben sie \"Schön\" ein", ValidationTyp.ERROR);
		}
		txt = customValWarn.getText();
		if (!"Schön".equals(txt)) {
			result.addViolation(customValWarn, "Bitte geben sie \"Schön\" ein", ValidationTyp.WARNING);
		}
		txt = customValInfo.getText();
		if (!"Schön".equals(txt)) {
			result.addViolation(customValInfo, "Bitte geben sie \"Schön\" ein", ValidationTyp.INFO);
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
