/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.demo.table;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
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
 * Demo für verschiedene Renderer und Editoren in Tabellen.
 * 
 * @author Scavenger156
 * 
 */
public class SampleController implements Initializable {
	@FXML
	private TableView<TestVO> mytableView;
	@FXML
	private TableColumn<TestVO, Boolean> colBoolean;
	@FXML
	private TableColumn<TestVO, String> colText;
	@FXML
	private TableColumn<TestVO, Boolean> colBooleanTooltip;
	@FXML
	private TableColumn<TestVO, String> colTextTooltip;

	private ObservableList<TestVO> data;

	/**
	 * 
	 * Konstruktor.
	 */
	public SampleController() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		colBoolean.setCellValueFactory(new PropertyValueFactory<TestVO, Boolean>("bool"));
		colText.setCellValueFactory(new PropertyValueFactory<TestVO, String>("zeichenkette"));
		// Textrenderer & Editor
		colText.setCellFactory(new Callback<TableColumn<TestVO, String>, TableCell<TestVO, String>>() {

			@Override
			public TableCell<TestVO, String> call(TableColumn<TestVO, String> param) {
				return new TextFieldCellEditor<>(new DefaultStringConverter());

			}
		});
		// Boolean editor + Renderer
		colBoolean.setCellFactory(new Callback<TableColumn<TestVO, Boolean>, TableCell<TestVO, Boolean>>() {
			@Override
			public TableCell<TestVO, Boolean> call(TableColumn<TestVO, Boolean> p) {
				return new BooleanCellEditor<>();
			}
		});
		// Tooltips auf Zellen
		colBooleanTooltip.setCellValueFactory(new PropertyValueFactory<TestVO, Boolean>("bool"));
		colTextTooltip.setCellValueFactory(new PropertyValueFactory<TestVO, String>("zeichenkette"));
		// Textrenderer & Editor
		colTextTooltip.setCellFactory(new Callback<TableColumn<TestVO, String>, TableCell<TestVO, String>>() {

			@Override
			public TableCell<TestVO, String> call(TableColumn<TestVO, String> param) {
				TextFieldCellEditor<TestVO, String> cell = new TextFieldCellEditor<TestVO, String>(new DefaultStringConverter()) {
					@Override
					public ObservableStringValue getTooltip(TestVO vo) {
						StringProperty tooltip = new SimpleStringProperty("Tooltip auf Zeile: ");
						StringExpression tip = tooltip.concat(indexProperty());
						return tip;
					}
				};
				return cell;
			}
		});
		// Boolean editor + Renderer
		colBooleanTooltip.setCellFactory(new Callback<TableColumn<TestVO, Boolean>, TableCell<TestVO, Boolean>>() {
			@Override
			public TableCell<TestVO, Boolean> call(TableColumn<TestVO, Boolean> p) {
				BooleanCellEditor<TestVO> cell = new BooleanCellEditor<TestVO>() {
					@Override
					public ObservableStringValue getTooltip(TestVO vo) {
						StringProperty tooltip = new SimpleStringProperty("Boolean Tooltip auf Zeile: ");
						StringExpression tip = tooltip.concat(indexProperty());
						return tip;
					}
				};

				return cell;
			}
		});

		// Das verändert das verhalten der Tabelle wenn eine Taste gedrückt wird
		TableEditHelper.install(mytableView);

		data = FXCollections.observableArrayList();
		data.add(new TestVO("123"));
		data.add(new TestVO("schön"));
		mytableView.setItems(data);

	}

}
