package sk.isdd.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sk.isdd.validator.controller.MasterController;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * JavaFX GUI application  with tools for transformation and validation around digital signatures.
 *
 * <p> User interface is divided into tabs which support different groups of functionalities.
 * Each tab is controlled by separate controller.
 * There is single master controller to propagate common features.
 */
public class ValidatorApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorApplication.class);

    /**
     * First stage (graphical window) created for this application.
     */
    private Stage stage;

    /**
     * JavaFX application starting point with given window.
     *
     * @param stage system initialized primary stage (main window, more can be added, if needed)
     */
    @Override
    public void start(Stage stage) {

        // Initialize layout
        try {
			FXMLLoader loader = new FXMLLoader();

            // I18n support
            ResourceBundle lang = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
            loader.setResources(lang);

            // Load root layout
			loader.setLocation(getClass().getResource("/fxml/master-screen.fxml"));

			// Attach scene to stage
            Scene scene = new Scene(new StackPane());
            scene.setRoot(loader.load());
			scene.getStylesheets().add("/styles/style.css");
			stage.setScene(scene);

            // Customize the stage
            stage.setTitle(lang.getString("appTitle"));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setResizable(true);
            stage.getIcons().add(new Image("/validator-logo.png"));
            stage.show();

			// Send stage to master controller
            MasterController controller = loader.getController();
            controller.setStage(stage);
            this.stage = stage;

        } catch (Exception e) {
            throw new ApplicationException("Unable to initialize layout: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(ValidatorApplication.class, args);
    }

    public Stage getStage() {
        return stage;
    }

}