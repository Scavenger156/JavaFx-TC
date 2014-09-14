package eu.thecreator.components;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Custom component with a moving point
 * 
 * @author andre
 *
 */
public class MyComponent extends Canvas {

    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty diameter = new SimpleDoubleProperty(20);

    public MyComponent() {
	// Main animation
	AnimationTimer timer = new AnimationTimer() {
	    @Override
	    public void handle(long now) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.BISQUE);
		gc.fillRect(0, 0, getWidth(), getHeight());
		gc.setFill(Color.BLUE);
		gc.fillOval(x.doubleValue(), y.doubleValue(),
			diameter.doubleValue(), diameter.doubleValue());

	    }
	};
	timer.start();
	createTimeLine();

    }

    /**
     * Timeline for the moving point
     */
    private void createTimeLine() {
	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
		new KeyValue(x, 0), new KeyValue(y, 0)), new KeyFrame(
		Duration.seconds(3), new KeyValue(x, widthProperty().subtract(
			diameter).doubleValue()), new KeyValue(y,
			heightProperty().subtract(diameter).doubleValue())));

	timeline.setOnFinished(new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(ActionEvent evt) {
		createTimeLine();
	    }
	});
	timeline.play();
    }

    public Double getX() {
	return x.get();
    }

    public void setX(Double value) {
	x.set(value);
    }

    public DoubleProperty xProperty() {
	return x;
    }

    public Double getY() {
	return x.get();
    }

    public void setY(Double value) {
	y.set(value);
    }

    public DoubleProperty yProperty() {
	return y;
    }

    public Double getDiameter() {
	return diameter.get();
    }

    public void setDiameter(Double value) {
	diameter.set(value);
    }

    public DoubleProperty diameterProperty() {
	return diameter;
    }
}
