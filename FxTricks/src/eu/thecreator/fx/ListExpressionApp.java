package eu.thecreator.fx;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListExpression;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import eu.thecreator.fx.util.DemoVO;
import eu.thecreator.fx.util.TableHelper;

public class ListExpressionApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

	TableView<DemoVO> table = TableHelper.createDummyTable();

	Label label = new Label();

	// Liste aller Items
	ListExpression<DemoVO> completeList = new ReadOnlyListWrapper<>(table.getItems());
	// Selektionen
	ListExpression<DemoVO> selection = new ReadOnlyListWrapper<>(table.getSelectionModel().getSelectedItems());
	// Text erstellen
	label.textProperty().bind(Bindings.concat(selection.sizeProperty()).concat("/").concat(completeList.sizeProperty()));

	BorderPane parent = new BorderPane(table, label, null, null, null);

	Scene scene = new Scene(parent);

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
