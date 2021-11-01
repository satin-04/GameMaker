/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.sprite;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.model.interfaces.Savable;
import gamemaker.utilities.JsonHelper;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RectangleSprite extends Sprite implements Savable {

	private Rectangle shape;

	// For loading only
	public RectangleSprite() {
		super();
		this.shape = new Rectangle();
		this.shape.setManaged(false); // MUST BE UNMANAGED!
	}

	public RectangleSprite(int id) {
		super(id);
		this.shape = new Rectangle();
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
		sb.append(JsonHelper.Serializer.serializePoint2D(Constants.DIMENSIONS_KEY,
				new Point2D(((Rectangle) shape).getWidth(), ((Rectangle) shape).getHeight())));

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
		setDimensions(JsonHelper.Deserializer.deserializePoint2D(Constants.DIMENSIONS_KEY, jsonObject));
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
		return Constants.RECTANGLE;
	}

	@Override
	public void setShape(Shape shape) {
		this.shape = (Rectangle) shape;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D(shape.getLayoutX(), shape.getLayoutY());
	}

	@Override
	public void setPosition(Point2D position) {
		shape.setLayoutX(position.getX());
		shape.setLayoutY(position.getY());
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
		return new Point2D(shape.getWidth(), shape.getHeight());
	}

	@Override
	public void setDimensions(Point2D dimensions) {
		shape.setWidth(dimensions.getX());
		shape.setHeight(dimensions.getY());
	}

	@Override
	public void setDimensionsX(double x) {
		shape.setWidth(x);
	}

	@Override
	public void setDimensionsY(double y) {
		shape.setWidth(y);
	}
}
