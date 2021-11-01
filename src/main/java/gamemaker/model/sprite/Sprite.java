package gamemaker.model.sprite;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.model.interfaces.Savable;
import gamemaker.utilities.JsonHelper;

import java.util.Objects;

public abstract class Sprite implements Savable {

	protected int id;
	protected String label;
	protected Point2D velocity;
	protected Color color;
	protected boolean visible;
	protected String text;

	// Not a game variable
	// For View/Model to know which sprite is selected
	protected boolean selected;

	// For loading only
	public Sprite() {
	}

	public Sprite(int id) {
		setLabel(Constants.DEFAULT_LABEL + id);
		setVisible(true);
		setVelocity(new Point2D(Constants.ZERO, Constants.ZERO));
	}

	public void updateProperties(String label, Point2D position, Point2D dimensions, Point2D velocity, Color color,
			boolean visible, String text) {
		setLabel(label);
		setPosition(position);
		setDimensions(dimensions);
		setVelocity(velocity);
		setColor(color);
		setVisible(visible);
		setText(text);
	}

	public void transferProperties(Sprite transferFromSprite) {
		setLabel(transferFromSprite.getLabel());
		setPosition(transferFromSprite.getPosition());
		setDimensions(transferFromSprite.getDimensions());
		setVelocity(transferFromSprite.getVelocity());
		setColor(transferFromSprite.getColor());
		setVisible(transferFromSprite.getVisible());
		setText(transferFromSprite.getText());
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

		sb.append(JsonHelper.Serializer.serializeInt(Constants.SPRITE_ID_KEY, id));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeString(Constants.LABEL_KEY, label));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializePoint2D(Constants.VELOCITY_KEY, velocity));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeString(Constants.COLOR_KEY, color.toString()));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeBoolean(Constants.VISIBLE_KEY, visible));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeString(Constants.TEXT_KEY, text));

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		setId(JsonHelper.Deserializer.deserializeInt(Constants.SPRITE_ID_KEY, jsonObject));
		setLabel(JsonHelper.Deserializer.deserializeString(Constants.LABEL_KEY, jsonObject));
		setVelocity(JsonHelper.Deserializer.deserializePoint2D(Constants.VELOCITY_KEY, jsonObject));
		setVisible(JsonHelper.Deserializer.deserializeBoolean(Constants.VISIBLE_KEY, jsonObject));
		setText(JsonHelper.Deserializer.deserializeString(Constants.TEXT_KEY, jsonObject));
	}

	/************************************
	 * 
	 * Getters/Setters/Adders/Removers
	 * 
	 ************************************/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.getShape().setId(String.valueOf(id));
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public abstract Shape getShape();

	public abstract String getShapeAsString();

	public abstract void setShape(Shape shape);

	public abstract Point2D getPosition();

	public abstract void setPosition(Point2D position);

	public abstract void setPositionX(double x);

	public abstract void setPositionY(double y);

	public abstract Point2D getDimensions();

	public abstract void setDimensions(Point2D dimensions);

	public abstract void setDimensionsX(double x);

	public abstract void setDimensionsY(double y);

	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}



	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		this.getShape().setFill(this.color);
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getSelected() {
		return visible;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (this.selected) {
			getShape().setStroke(Constants.SELECTED_STROKE_COLOR);
		} else {
			getShape().setStroke(Constants.UNSELECTED_STROKE_COLOR);
		}
	}

	/**
	 * Checks if every property between the two Sprites is equal.
	 * The spriteId will not be checked, but the following fields will be:
	 * <ul>
	 * 	<li>visibility</li>
	 * 	<li>label</li>
	 * 	<li>velocity</li>
	 * 	<li>color</li>
	 * 	<li>test</li></ul>
	 * @return true if all instance variables are equal or if the
	 * Sprites have the same memory addresses.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Sprite sprite = (Sprite) o;
		return visible == sprite.visible &&
				Objects.equals(label, sprite.label) &&
				velocity.equals(sprite.velocity) &&
				color.equals(sprite.color) &&
				Objects.equals(text, sprite.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, label, velocity, color, visible, text, selected);
	}

	//	public Point2D getCenter(Point2D position) {
//        return new Point2D(position.getX() + dimensions.getX() / 2, position.getY() + dimensions.getY() / 2);
//    }
//    
//    public Point2D getUpperLeft(Point2D position) {
//        return new Point2D(getCenter(position).getX() - dimensions.getX() / 2, getCenter(position).getY() - dimensions.getY() / 2);
//    }
//    
//    public Point2D getLowerLeft(Point2D position) {
//        return new Point2D(getCenter(position).getX() - dimensions.getX() / 2, getCenter(position).getY() + dimensions.getY() / 2);
//    }
//    
//    public Point2D getUpperRight(Point2D position) {
//        return new Point2D(getCenter(position).getX() + dimensions.getX() / 2, getCenter(position).getY() - dimensions.getY() / 2);
//    }
//    
//    public Point2D getLowerRight(Point2D position) {
//        return new Point2D(getCenter(position).getX() + dimensions.getX() / 2, getCenter(position).getY() + dimensions.getY() / 2);
//    }
//
//	public Point2D getPreviousPosition() {
//		return position;
//	}
//
//	public Point2D getNextPosition() {
//		Point2D next_position = new Point2D(position.getX()+velocity.getX(),position.getY()+velocity.getY());
//		return next_position;
//	}
}
