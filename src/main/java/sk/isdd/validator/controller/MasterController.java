package sk.isdd.validator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import sk.isdd.validator.ApplicationException;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Master controller for all the tabs
 * <p>
 * Not much logic for now, just propagating and syncing objects
 */
public class MasterController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabDigest;

    /**
     * Controller class to process digest messages tab
     */
    @FXML
    private DigestController digestController;

    private Stage stage;

    /**
     * Set master stage for controller and it's sub-controllers
     *
     * @param stage master stage reserved for application
     */
    public void setStage(Stage stage) {
        this.stage = stage;

        // initialize children tabs
        // TODO: this works, but there should be better way to propagate objects for sub controllers (like autowire via constructor?)
        if (digestController == null) {
            throw new ApplicationException("DigestController is not loaded");
        }
        digestController.setStage(stage);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
