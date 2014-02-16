package eu.thecreator.scenebuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class Main {
	public static void main(String[] args) {

		try {
			FileInputStream configFile = new FileInputStream("logging.properties");
			LogManager.getLogManager().readConfiguration(configFile);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		com.javafx.main.Main.main(args);
	}
}
