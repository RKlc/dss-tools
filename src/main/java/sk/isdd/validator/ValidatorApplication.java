package sk.isdd.validator;

import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sk.isdd.validator.controller.MasterController;


public class ValidatorApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorApplication.class);

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.stage.setTitle("DSS Tools");
        this.stage.setResizable(true);
        this.stage.getIcons().add(new Image("/validator-logo.png"));

        initLayout();
    }

    private void initLayout() {
        try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/master-screen.fxml"));

            Scene scene = new Scene(new StackPane());
            scene.setRoot(loader.load());
			scene.getStylesheets().add("/styles/style.css");

			stage.setScene(scene);
			stage.show();

            MasterController controller = loader.getController();
            controller.setStage(stage);

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
