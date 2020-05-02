package sk.isdd.validator.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sk.isdd.validator.ApplicationException;

import java.net.URL;
import java.util.ResourceBundle;

public class MasterController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabDigest;

    @FXML
    private DigestController digestController;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;

        // initialize children tabs
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
