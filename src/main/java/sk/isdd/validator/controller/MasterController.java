package sk.isdd.validator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Master controller for the whole application.
 * <p>
 * Contains all the tabs. Delegates control to sub controllers if needed.
 * Not much logic for now, just propagating and syncing objects.
 */
public class MasterController {

    /**
     * Master tab pane where the whole application resides. It is used as reference to obtain stage from.
     */
    @FXML
    private TabPane appWindow;

    /**
     * Tab for "digest messages" group of functionality. It has its own controller.
     */
    @FXML
    private Tab tabDigest;

    /**
     * Controller class to process digest messages on tabDigest. Autoinjected from FXML resource fx:id="digest".
     */
    @FXML
    private DigestController digestController;

    /**
     * Primary stage (window) used for this controller. Passed from main app.
     */
    private Stage stage;

    /**
     * Sets the master stage (window) for this controller and its sub-controllers.
     *
     * @param stage the stage intended for this controller
     */
    public void setStage(Stage stage) {

        this.stage = stage;

        // set same stage for all intended controllers
        digestController.setStage(stage);
    }

    public Stage getStage()
    {
        return stage;
    }
}
