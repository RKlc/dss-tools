package sk.isdd.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.isdd.validator.controller.MasterController;


/**
 * JavaFX application initialization
 * <p>
 * User interface is divided into tabs and supported by different controllers.
 * There is single master controller to propagate common features.
 */
public class ValidatorApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorApplication.class);

    /**
     * Stage (graphical workspace) dedicated to this application
     */
    private Stage stage;

    /**
     * JavaFX application starting point with given workspace
     *
     * @param stage the stage for all controllers
     */
    @Override
    public void start(Stage stage) {

        // Basic window setup
        stage.setTitle("DSS Tools");
        stage.setResizable(true);
        stage.getIcons().add(new Image("/validator-logo.png"));
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Initialize layout
        try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/master-screen.fxml"));

            Scene scene = new Scene(new StackPane());
            scene.setRoot(loader.load());
			scene.getStylesheets().add("/styles/style.css");

			stage.setScene(scene);
			stage.show();

			// Single stage passed down to all controllers where it is customized and used
            MasterController controller = loader.getController();
            controller.setStage(stage);
            this.stage = stage;

        } catch (Exception e) {
            throw new ApplicationException("Unable to initialize layout : " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(ValidatorApplication.class, args);
    }

    public Stage getStage() {
        return stage;
    }

}