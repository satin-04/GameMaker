package gamemaker;

import gamemaker.controller.Controller;
import gamemaker.model.Model;
import gamemaker.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class GameMakerApplication extends Application {

	public static final Logger logger = Logger.getLogger(GameMakerApplication.class);
	private static Scene scene;
	private static Model model;

	@Override
	public void start(Stage stage) throws IOException {
		// log4j configuration
		BasicConfigurator.configure();

		// Load the loader
		FXMLLoader fxmlLoader = new FXMLLoader(GameMakerApplication.class.getResource("game-maker-view-v2.fxml"));
		// Load the scene
		scene = new Scene(fxmlLoader.load());
		// Load css for the scene
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		
		// Get View from the loader
		View view = (View) fxmlLoader.getController();
		
		// Create the Model
		model = new Model(view);
		// Give View gameCanvas reference
		view.setGameCanvas(model.getGameCanvas());
		// Now set up Drag Controller
		view.setupDragController();
		
		// Create the Controller
		Controller controller = new Controller(model, view);
		// Give View Controller Reference
		view.setController(controller);

		// Finish set up and show
		stage.setTitle("GameMaker");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Returns the current scene of the application.
	 * If the scene is null, returns a Scene with an empty Parent.
	 */
	public static Scene getScene() {
		if (scene == null) {
			logger.fatal("WARNING: Scene was null. Returning a useless Scene instead to avoid null-pointer.");
			scene = new Scene(new Parent() {});
		}
		return scene;
	}

	public static Model getModel() {
		return model;
	}

	public static void main(String[] args) {
		launch();
	}
}