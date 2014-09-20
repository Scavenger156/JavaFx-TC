package eu.thecreator.fx.util;

import javafx.beans.property.SimpleStringProperty;

public class DemoVO {
    private SimpleStringProperty text = new SimpleStringProperty();

    public DemoVO(String txt) {
	setText(txt);
    }

    /**
     * @return the Property
     **/
    public SimpleStringProperty textProperty() {
	return text;
    }

    /**
     * @return the Value
     **/
    public String getText() {
	return text.get();
    }

    /**
     * @param newValue
     *            the Value
     **/
    public void setText(String newValue) {
	this.text.set(newValue);
    }
}
