/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.sprite;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.model.interfaces.Savable;
import gamemaker.utilities.JsonHelper;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextSprite extends Sprite implements Savable {

	private Text shape;

	// For loading only
	public TextSprite() {
		super();
		this.shape = new Text();
		this.shape.setManaged(false); // MUST BE UNMANAGED!
		this.shape.setFont(new Font(Constants.DEFAULT_FONT, Constants.DEFAULT_FONT_SIZE));
	}

	public TextSprite(int id) {
		super(id);
		this.shape = new Text();
		this.shape.setManaged(false); // MUST BE UNMANAGED!
		this.shape.setFont(new Font(Constants.DEFAULT_FONT, Constants.DEFAULT_FONT_SIZE));

		setId(id);
		setPosition(
				new Point2D(Constants.GAME_CANVAS_WIDTH / Constants.TWO, Constants.GAME_CANVAS_HEIGHT / Constants.TWO));
		setDimensions(new Point2D(Constants.DEFAULT_SPRITE_SIZE, Constants.DEFAULT_SPRITE_SIZE));
		setColor(Constants.DEFAULT_SPRITE_COLOR);
		setText(Constants.EMPTY_STRING);
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
				new Point2D(((Text) shape).getFont().getSize(), ((Text) shape).getFont().getSize())));

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
		return Constants.TEXT;
	}

	@Override
	public void setShape(Shape shape) {
		this.shape = (Text) shape;
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
		return new Point2D(shape.getFont().getSize(), shape.getFont().getSize());
	}

	@Override
	public void setDimensions(Point2D dimensions) {
		this.shape.setFont(new Font(Constants.DEFAULT_FONT, Constants.DEFAULT_FONT_SIZE));
//		this.shape.setFont(new Font(Constants.DEFAULT_FONT, Constants.DEFAULT_FONT_SIZE));

//		shape.setWidth(dimensions.getX());
//		shape.setHeight(dimensions.getY());
	}

	@Override
	public void setDimensionsX(double x) {
		throw new UnsupportedOperationException("Text Sprite doesn't support setDimensionX");
	}

	@Override
	public void setDimensionsY(double y) {
		throw new UnsupportedOperationException("Text Sprite doesn't support setDimensionX");
	}

	@Override
	public void setText(String text) {
		this.text = text;
		this.shape.setText(text);
	}
}