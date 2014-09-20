package eu.thecreator.fx.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableHelper {
    public static TableView<DemoVO> createDummyTable() {
	List<DemoVO> data = new ArrayList<>();
	data.add(new DemoVO("1"));
	data.add(new DemoVO("2"));
	data.add(new DemoVO("3"));
	data.add(new DemoVO("4"));
	data.add(new DemoVO("5"));

	TableView<DemoVO> tbl = new TableView<>();
	tbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	tbl.getItems().setAll(data);

	TableColumn<DemoVO, String> column = new TableColumn<>("Text");
	column.setCellValueFactory(new PropertyValueFactory<>("text"));
	column.setPrefWidth(180);

	tbl.getColumns().add(column);

	return tbl;
    }
}
