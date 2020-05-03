package sk.isdd.validator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.isdd.validator.ApplicationException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Master controller for whole application.
 * <p>
 * Contains all the tabs. Delegates control to sub controllers if needed.
 * Not much logic for now, just propagating and syncing objects.
 */
public class MasterController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabDigest;

    /**
     * Controller class to process digest messages in its own tab. Auto-wired from FXML resource file.
     */
    @FXML
    private DigestController digestController;

    private Stage stage;

    /**
     * Set master stage for controller and it's sub-controllers.
     *
     * @param stage the master stage reserved for the application
     */
    public void setStage(Stage stage) {

        // Customize the application stage
        stage.setTitle("DSS Tools");
        stage.setResizable(true);
        stage.getIcons().add(new Image("/validator-logo.png"));
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Initialize children tabs
        // TODO: this works, but there should be better way to propagate objects for sub controllers (like autowire via constructor?)
        if (digestController == null) {
            throw new ApplicationException("DigestController is not loaded");
        }
        digestController.setStage(stage);

        // Save for later use
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * FX initializable has access to all the @FXML annotations, constructor does not.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
