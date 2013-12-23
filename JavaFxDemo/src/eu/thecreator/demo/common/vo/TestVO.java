package eu.thecreator.demo.common.vo;

import java.util.Objects;
import java.util.logging.Logger;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Testklasse
 * 
 * @author Scavenger156
 * 
 */
public class TestVO {

	private SimpleStringProperty zeichenkette = new SimpleStringProperty();
	private SimpleIntegerProperty zahl = new SimpleIntegerProperty();

	private SimpleBooleanProperty bool = new SimpleBooleanProperty();

	private Logger logger = Logger.getLogger(TestVO.class.getName());

	public SimpleStringProperty zeichenketteProperty() {

		return zeichenkette;
	}

	public TestVO() {

		ChangeListener<Object> cl = new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				logger.info(newValue != null ? newValue.toString() : null);
			}
		};
		zeichenkette.addListener(cl);
		zahl.addListener(cl);
		bool.addListener(cl);

	}

	public TestVO(String zeichenkette) {
		this();
		setZeichenkette(zeichenkette);
	}

	public SimpleIntegerProperty getPzahl() {
		return zahl;
	}

	public SimpleBooleanProperty boolProperty() {
		return bool;
	}

	public Boolean getBool() {
		return bool.get();
	}

	public void setBool(Boolean bool) {
		this.bool.set(bool);
	}

	public Integer getZahl() {
		return zahl.get();
	}

	public void setZahl(Integer zahl) {
		this.zahl.set(zahl);
	}

	public String getZeichenkette() {
		return zeichenkette.get();
	}

	public void setZeichenkette(String zeichenkette) {
		this.zeichenkette.set(zeichenkette);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 83 * hash + Objects.hashCode(this.zeichenkette);
		hash = 83 * hash + Objects.hashCode(this.zahl);
		hash = 83 * hash + Objects.hashCode(this.bool);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TestVO other = (TestVO) obj;
		if (!Objects.equals(this.zeichenkette, other.zeichenkette)) {
			return false;
		}
		if (!Objects.equals(this.zahl, other.zahl)) {
			return false;
		}
		if (!Objects.equals(this.bool, other.bool)) {
			return false;
		}

		return true;
	}
}
