/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/

package gamemaker.view;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 ** How can we make this View logic? This is breaking MVC rules. Because it is
 * performing View logic on one of the Model's data variables
 **/
public class CanvasInputHandler {

	// Our listener for mouse events
	private View view;

	// What was clicked
	private Node target;

	// For calculating position when pressed/dragging/released
	private double anchorX;
	private double anchorY;
	private double initialPositionX;
	private double initialPositionY;

	// The way to turn off dragging when the game starts
	private BooleanProperty isDraggable;

	// CanvasInputManager
	public CanvasInputHandler(View view) {
		this.view = view;
		createDraggableProperty();
		this.isDraggable.set(true);
	}

	private void mouseClickAction(MouseEvent event) {
		target = (Node) event.getTarget();
		// Basically ignore if user hit the game canvas
		if (target.getId().compareTo(Constants.GAME_CANVAS_ID) != 0) {
			GameMakerApplication.logger.info("Mouse Event Click's target is: " + target.toString());
			if (event.isPrimaryButtonDown()) {
				anchorX = event.getSceneX();
				anchorY = event.getSceneY();
				initialPositionX = target.getLayoutX();
				initialPositionY = target.getLayoutY();
				// replace this with a direct call to the command
				view.setSelectedTarget(target);
			}
		} else {
			target = null;
		}
	}

	private void mouseDragAction(MouseEvent event) {
		if (target != null) {
			// TODO figure out these values properly...
			// This only works if the Sprite is exactly in the middle of the screen
			// Must be dynamic to work from any position
//			double clampedPositionX = Math.max(-Constants.GAME_CANVAS_WIDTH/2, Math.min(Constants.GAME_CANVAS_WIDTH, event.getSceneX() - anchorX));
//			double clampedPositionY = Math.max(-Constants.GAME_CANVAS_HEIGHT/2, Math.min(Constants.GAME_CANVAS_HEIGHT, event.getSceneY() - anchorY));

			view.dragTarget(event.getSceneX() - anchorX, event.getSceneY() - anchorY);
		}
	}

	private void mouseReleaseAction(MouseEvent event) {
		if (target != null) {
			GameMakerApplication.logger.info("Mouse Release Action! " + initialPositionX + target.getTranslateX() + " " + initialPositionY + target.getTranslateY());
			view.releaseTarget(initialPositionX + target.getTranslateX(), initialPositionY + target.getTranslateY(),
					initialPositionX, initialPositionY);
		}
	}

	// This controls adding/remove EventFilters when we set isDraggable to true or
	// false. We do this when App loads and Play/Stop is pressed
	public void createDraggableProperty() {
		isDraggable = new SimpleBooleanProperty();
		isDraggable.addListener(this::setMouseEvents);
	}

	private void setMouseEvents(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (newValue) {
			GameMakerApplication.logger.info("Adding drag events");
//			view.getGameCanvas().addEventFilter(MouseEvent.MOUSE_PRESSED, this::mouseClickAction);
//			view.getGameCanvas().addEventFilter(MouseEvent.MOUSE_DRAGGED, this::mouseDragAction);
//			view.getGameCanvas().addEventFilter(MouseEvent.MOUSE_RELEASED, this::mouseReleaseAction);
			
			view.getGameCanvas().setOnMousePressed(this::mouseClickAction);
			view.getGameCanvas().setOnMouseDragged(this::mouseDragAction);
			view.getGameCanvas().setOnMouseReleased(this::mouseReleaseAction);			
		} else {
			GameMakerApplication.logger.info("Removing drag events");
//			view.getGameCanvas().removeEventFilter(MouseEvent.MOUSE_PRESSED, this::mouseClickAction);
//			view.getGameCanvas().removeEventFilter(MouseEvent.MOUSE_DRAGGED, this::mouseDragAction);
//			view.getGameCanvas().removeEventFilter(MouseEvent.MOUSE_RELEASED, this::mouseReleaseAction);

			view.getGameCanvas().setOnMousePressed(null);
			view.getGameCanvas().setOnMouseDragged(null);
			view.getGameCanvas().setOnMouseReleased(null);
		}
	}

	public boolean getIsDraggable() {
		return isDraggable.get();
	}

	public void setIsDraggable(boolean draggable) {
		isDraggable.set(draggable);
	}
}