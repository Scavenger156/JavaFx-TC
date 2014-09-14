package eu.thecreator.demo.validation.hibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Demo für die Validatoren von Hibernate.
 * 
 * @author Scavenger156
 * 
 */
public class ValidatorHibernateApp extends Application {
	/**
	 * 
	 * Konstruktor.
	 */
	public ValidatorHibernateApp() {

	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Main zum Starten der Demo
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
