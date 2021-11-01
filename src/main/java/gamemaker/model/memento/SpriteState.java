/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.memento;

import gamemaker.GameMakerApplication;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class SpriteState {

	private int spriteId;
	private Point2D position;
	private Point2D dimensions;
	private Point2D velocity;
	private Color color;
	private boolean visible;
	private String text;

	public SpriteState() {
	}

	public void storeSpriteState(Sprite sprite) {
		// Sprite id is just to check
		// not for restoring!
		setSpriteId(sprite.getId());
		setPosition(sprite.getPosition());
		setDimensions(sprite.getDimensions());
		setVelocity(sprite.getVelocity());
		setColor(sprite.getColor());
		setVisible(sprite.getVisible());
		setText(sprite.getText());
	}
	
	public void restoreSpriteState(Sprite sprite) {
		if(sprite.getId() != getSpriteId()) {
			GameMakerApplication.logger.fatal("Sprite and SpriteState are mismatched! Ids do not match! SpriteState id: " + getSpriteId() + " Sprite id: " + sprite.getId());
			throw new UnsupportedOperationException("Sprite and SpriteState are mismatched! Ids do not match! SpriteState id: " + getSpriteId() + " Sprite id: " + sprite.getId());
		}
		sprite.setPosition(getPosition());
		sprite.setDimensions(getDimensions());
		sprite.setVelocity(getVelocity());
		sprite.setColor(getColor());
		sprite.setVisible(getVisible());
		sprite.setText(getText());
	}

	private int getSpriteId() {
		return spriteId;
	}

	private void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}
	
	private Point2D getPosition() {
		return position;
	}

	private void setPosition(Point2D position) {
		this.position = position;
	}

	private Point2D getDimensions() {
		return dimensions;
	}

	private void setDimensions(Point2D dimensions) {
		this.dimensions = dimensions;
	}

	private Point2D getVelocity() {
		return velocity;
	}

	private void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}

	private Color getColor() {
		return color;
	}

	private void setColor(Color color) {
		this.color = color;
	}

	private boolean getVisible() {
		return visible;
	}

	private void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	private String getText() {
		return text;
	}

	private void setText(String text) {
		this.text = text;
	}
}
