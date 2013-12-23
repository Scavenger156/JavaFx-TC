/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.demo.validation.table;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import eu.thecreator.components.table.TableEditHelper;
import eu.thecreator.components.table.cell.BooleanCellEditor;
import eu.thecreator.components.table.cell.TextFieldCellEditor;
import eu.thecreator.demo.common.vo.TestVO;

/**
 * Demo für die Validatoren in einer Tabellenzelle.
 * 
 * @author Scavenger156
 * 
 */
public class SampleController implements Initializable {
	@FXML
	private TableView<TestVO> mytableView;
	@FXML
	private TableColumn<TestVO, Boolean> col1;
	@FXML
	private TableColumn<TestVO, String> col2;
	private ObservableList<TestVO> data;

	/**
	 * 
	 * Konstruktor.
	 */
	public SampleController() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		col1.setCellValueFactory(new PropertyValueFactory<TestVO, Boolean>("bool"));
		col2.setCellValueFactory(new PropertyValueFactory<TestVO, String>("zeichenkette"));

		col2.setCellFactory(new Callback<TableColumn<TestVO, String>, TableCell<TestVO, String>>() {

			@Override
			public TableCell<TestVO, String> call(TableColumn<TestVO, String> param) {
				return new TextFieldCellEditor<TestVO, String>(new DefaultStringConverter());

			}
		});

		col1.setCellFactory(new Callback<TableColumn<TestVO, Boolean>, TableCell<TestVO, Boolean>>() {
			@Override
			public TableCell<TestVO, Boolean> call(TableColumn<TestVO, Boolean> p) {
				return new BooleanCellEditor<>();
			}
		});

		TableEditHelper<TestVO> helper = new TableEditHelper<>(mytableView);
		helper.init();
		data = FXCollections.observableArrayList();
		data.add(new TestVO());
		data.add(new TestVO());
		mytableView.setItems(data);

		// TODO Validatoren
	}

}
