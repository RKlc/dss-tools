package sk.isdd.validator.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.DigestAlgorithm;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.fx.FileToStringConverter;
import sk.isdd.validator.model.DigestModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class DigestController implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(DigestController.class);

	@FXML
	public ComboBox<XmlC14nMethod> comboMethod;

    @FXML
    public Label lblMethodUri;

	@FXML
	public ComboBox<DigestAlgorithm> comboAlgorithm;

    @FXML
	private Button sourceFileButton;

	@FXML
	private Button calculateButton;

	@FXML
	private Button saveButton;

	private Stage stage;

	private DigestModel model;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void initialize(URL location, ResourceBundle resources) {
		model = new DigestModel();

        // init XML source file button
		sourceFileButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select source file");
			File sourceFile = fileChooser.showOpenDialog(stage);
			model.setSourceFile(sourceFile);
		});
		sourceFileButton.textProperty().bindBidirectional(model.sourceFileProperty(), new FileToStringConverter());
        BooleanBinding isMandatoryFieldsEmpty = model.sourceFileProperty().isNull();

        // init c14n combo box
        comboMethod.valueProperty().bindBidirectional(model.methodProperty());
        comboMethod.getItems().setAll(XmlC14nMethod.values());

        // add listener to c14n combo box value and update adjacent label with uri value
        comboMethod.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    lblMethodUri.setText(newValue.getUri());
                }
        );
        comboMethod.setValue(XmlC14nMethod.NONE);

        // init calculation button
		calculateButton.disableProperty().bind(isMandatoryFieldsEmpty);

		calculateButton.setOnAction(event -> {

			File sourceFile = model.getSourceFile();

			//init array with file length
			byte[] sourceArray = new byte[(int) sourceFile.length()];

			FileInputStream fis = null;

			try {
				fis = new FileInputStream(sourceFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}

			try {
				//read file into bytes[]
				fis.read(sourceArray);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			byte[] outArray;

			try {
				Init.init();
				Canonicalizer c14n = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
				outArray = c14n.canonicalize(sourceArray);
			} catch (Exception e) {
				throw new RuntimeException("Cannot canonicalize the binaries", e);
			}

			try {
				FileUtils.writeByteArrayToFile(new File("D:\\out.xml"), outArray);
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR, "Unable to save file: " + e.getMessage(), ButtonType.CLOSE);
				alert.showAndWait();
			}
		});

	}
}
