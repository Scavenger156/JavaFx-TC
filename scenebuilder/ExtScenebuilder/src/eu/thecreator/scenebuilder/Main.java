package eu.thecreator.scenebuilder;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

import com.oracle.javafx.scenebuilder.app.SceneBuilderApp;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import com.oracle.javafx.scenebuilder.kit.fxom.FXOMInstance;
import com.oracle.javafx.scenebuilder.kit.library.LibraryItem;

import eu.thecreator.components.MyComponent;

/**
 * Overriding the existing Scenebuilder
 * 
 * @author andre
 *
 */
public class Main extends SceneBuilderApp {

	private static final String CUSTOMLIB_NAME = "Customlib";

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		super.start(arg0);
		postLoad();
	}

	/**
	 * Adding our own code
	 */
	private void postLoad() {
		// These are the current items of the library
		ObservableList<LibraryItem> items = SceneBuilderApp.getSingleton()
				.getUserLibrary().getItems();
		if (items.isEmpty()) {
			// The items are not loaded so we try again later
			javafx.application.Platform.runLater(new Runnable() {

				@Override
				public void run() {
					postLoad();
				}
			});
			return;
		}

		// adding our Components
		items.add(addComponent(MyComponent.class));

	}

	private static LibraryItem addComponent(final Class<?> componentClass) {
		LibraryItem item = new LibraryItem(componentClass.getSimpleName(),
				CUSTOMLIB_NAME, 
				// 
				"" // The parameter could not be "null" otherwise
				   // drag and drop would not work
				, null, SceneBuilderApp.getSingleton().getUserLibrary()) {
			@Override
			public FXOMDocument instantiate() {
				FXOMDocument document = new FXOMDocument();

				FXOMInstance in = new FXOMInstance(document, componentClass);
				document.setFxomRoot(in);
				
				return document;
			}

		};
		return item;
	}
}
