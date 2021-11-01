/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import java.util.HashMap;
import java.util.Objects;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.Model;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class UpdateSelectedSpritePropertiesCommand extends Command {

	private Model model;

	private String undoLabel;
	private Point2D undoPosition;
	private Point2D undoDimensions;
	private Point2D undoVelocity;
	private Color undoColor;
	private boolean undoVisible;
	private String undoShape;
	private String undoText;

	private String label;
	private Point2D position;
	private Point2D dimensions;
	private Point2D velocity;
	private Color color;
	private boolean visible;
	private String shape;
	private String text;

	private String redoLabel;
	private Point2D redoPosition;
	private Point2D redoDimensions;
	private Point2D redoVelocity;
	private Color redoColor;
	private boolean redoVisible;
	private String redoShape;
	private String redoText;


	public UpdateSelectedSpritePropertiesCommand(Model model, HashMap<String, String> propertyNameToValue) {
		setPushToUndoStack(true);
		this.model = model;

		//Parse the string data from the HashMap into various data
		label = propertyNameToValue.get(Constants.LABEL_KEY);
		position = new Point2D(Integer.parseInt(propertyNameToValue.get(Constants.POSITION_X_KEY)),
				Integer.parseInt(propertyNameToValue.get(Constants.POSITION_Y_KEY)));
		dimensions = new Point2D(Integer.parseInt(propertyNameToValue.get(Constants.DIMENSIONS_X_KEY)),
				Integer.parseInt(propertyNameToValue.get(Constants.DIMENSIONS_Y_KEY)));
		velocity = new Point2D(Integer.parseInt(propertyNameToValue.get(Constants.VELOCITY_X_KEY)),
				Integer.parseInt(propertyNameToValue.get(Constants.VELOCITY_Y_KEY)));
		color = Color.web(propertyNameToValue.get(Constants.COLOR_KEY));
		visible = Boolean.parseBoolean(propertyNameToValue.get(Constants.VISIBLE_KEY));
		shape = propertyNameToValue.get(Constants.SHAPE_KEY);
		text = propertyNameToValue.get(Constants.TEXT_KEY);
	}

	@Override
	public void execute() {
		Sprite undoSprite = model.getCurrentSelectedSprite();
		try {
			undoLabel = undoSprite.getLabel();
			undoPosition = undoSprite.getPosition();
			undoDimensions = undoSprite.getDimensions();
			undoVelocity = undoSprite.getVelocity();
			undoColor = undoSprite.getColor();
			undoVisible = undoSprite.getVisible();
			undoShape = undoSprite.getShapeAsString();

			model.updateSelectedSpriteProperties(label, position, dimensions, velocity, color, visible, shape, text);

			// We have to do this just in case shape changed the sprite
			// Shape causes a new sprite to be created
			Sprite redoSprite = model.getCurrentSelectedSprite();
			redoLabel = redoSprite.getLabel();
			redoPosition = redoSprite.getPosition();
			redoDimensions = redoSprite.getDimensions();
			redoVelocity = redoSprite.getVelocity();
			redoColor = redoSprite.getColor();
			redoVisible = redoSprite.getVisible();
			redoShape = redoSprite.getShapeAsString();
		}
		catch (NullPointerException ex)
		{
			ex.printStackTrace();
			GameMakerApplication.logger.error("One or more view elements may have been null during execute() of UpdateSelectedSpritePropertiesCommand");
		}
	}

	@Override
	public void undo() {
		model.updateSelectedSpriteProperties(undoLabel, undoPosition, undoDimensions, undoVelocity, undoColor, undoVisible,
				undoShape, undoText);
	}

	@Override
	public void redo() {
		model.updateSelectedSpriteProperties(redoLabel, redoPosition, redoDimensions, redoVelocity, redoColor, redoVisible,
				redoShape, redoText);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UpdateSelectedSpritePropertiesCommand that = (UpdateSelectedSpritePropertiesCommand) o;
		return visible == that.visible && label.equals(that.label) && position.equals(that.position) && dimensions.equals(that.dimensions) && velocity.equals(that.velocity) && color.equals(that.color) && shape.equals(that.shape) && text.equals(that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(label, position, dimensions, velocity, color, visible, shape, text);
	}
}
