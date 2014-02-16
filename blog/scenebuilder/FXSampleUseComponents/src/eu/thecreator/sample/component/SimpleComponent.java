package eu.thecreator.sample.component;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SimpleComponent extends AnchorPane {
	@FXML
	private Label label;

	@FXML
	private TextField textField;

	public SimpleComponent() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SimpleComponent.fxml"));
		fxmlLoader.setRoot(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}
}
