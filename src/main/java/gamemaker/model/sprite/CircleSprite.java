/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.sprite;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.interfaces.Savable;
import gamemaker.utilities.JsonHelper;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.Objects;

public class CircleSprite extends Sprite implements Savable {

	private Circle shape;

	// For loading only
	public CircleSprite() {
		super();
		this.shape = new Circle();
		this.shape.setManaged(false); // MUST BE UNMANAGED!
	}

	public CircleSprite(int id) {
		super(id);
		this.shape = new Circle();
		this.shape.setManaged(false); // MUST BE UNMANAGED!
		
		setId(id);
		setPosition(
				new Point2D(Constants.GAME_CANVAS_WIDTH / Constants.TWO, Constants.GAME_CANVAS_HEIGHT / Constants.TWO));
		setDimensions(new Point2D(Constants.DEFAULT_SPRITE_SIZE, Constants.DEFAULT_SPRITE_SIZE));
		setColor(Constants.DEFAULT_SPRITE_COLOR);
	}

	/************************************
	 * 
	 * Savable Implementations
	 *
	 ************************************/

	@Override
	public String save(boolean encloseMyself) {
		StringBuilder sb = new StringBuilder();

		if (encloseMyself) {
			sb.append("{");
		}

		sb.append(super.save(false));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeString(Constants.CLASS_KEY, this.getClass().getTypeName()));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializePoint2D(Constants.POSITION_KEY,
				new Point2D(shape.getLayoutX(), shape.getLayoutY())));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeDouble(Constants.RADIUS_KEY, ((Circle) shape).getRadius()));

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		super.load(jsonObject);
		this.shape.setManaged(false); // MUST BE UNMANAGED!
		
		setPosition(JsonHelper.Deserializer.deserializePoint2D(Constants.POSITION_KEY, jsonObject));
		double radius = JsonHelper.Deserializer.deserializeDouble(Constants.RADIUS_KEY, jsonObject);
		setDimensions(new Point2D(radius, radius));
		setColor(JsonHelper.Deserializer.deserializeColor(Constants.COLOR_KEY, jsonObject));
	}

	/************************************
	 * 
	 * Getters/Setters
	 *
	 ************************************/

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public String getShapeAsString() {
		return Constants.CIRCLE;
	}

	@Override
	public void setShape(Shape shape) {
		this.shape = (Circle) shape;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D(shape.getLayoutX(), shape.getLayoutY());
	}

	@Override
	public void setPosition(Point2D position) {
		shape.setLayoutX(position.getX());
		shape.setLayoutY(position.getY());
		GameMakerApplication.logger.info("Setting sprite position");
	}

	@Override
	public void setPositionX(double x) {
		shape.setLayoutX(x);
	}

	@Override
	public void setPositionY(double y) {
		shape.setLayoutY(y);
	}

	@Override
	public Point2D getDimensions() {
		return new Point2D(shape.getRadius(), shape.getRadius());
	}

	@Override
	public void setDimensions(Point2D dimensions) {
		double radius = dimensions.getX();
		shape.setRadius(radius);
	}

	@Override
	public void setDimensionsX(double x) {
		throw new UnsupportedOperationException("Circle Sprite doesn't support setDimensionX");

	}

	@Override
	public void setDimensionsY(double y) {
		throw new UnsupportedOperationException("Circle Sprite doesn't support setDimensionY");
	}

	/*@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CircleSprite that = (CircleSprite) o;
		return Objects.equals(shape, that.shape) && super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), shape);
	}*/
}
