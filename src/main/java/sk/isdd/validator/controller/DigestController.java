package sk.isdd.validator.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.fx.FileToStringConverter;
import sk.isdd.validator.model.DigestModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for message digests.
 * <p>
 * User can select source file and optional c14n canonical transformation.
 * All the digests are calculated at once, over transformed source file.
 * Resulting XML file can be saved as new file.
 * <ul>
 * <li>Supports c14n transformation method (canonicalization) as defined by W3C,
 *      enumerated in {@link sk.isdd.validator.enumerations.XmlC14nMethod}.
 * <li>Supports all the hash algorithms enumerated in {@link sk.isdd.validator.enumerations.DigestAlgorithm}
 * </ul>
 */
public class DigestController implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(DigestController.class);

    /**
     * Combo box for selecting canonicalization method (enumerated in {@link sk.isdd.validator.enumerations.XmlC14nMethod})
     */
	@FXML
	public ComboBox<XmlC14nMethod> cbXmlC14nMethod;

    /**
     * Label holding information about W3C URI specification from selected enumeration
     */
    @FXML
    public Label lblMethodUri;

    @FXML
	private Button btnSourceFile;

	@FXML
	private Button btnCalculate;

	@FXML
	private Button btnSaveAs;

	private Stage stage;
	private DigestModel model;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new DigestModel();

        // init loading button for XML source file
		btnSourceFile.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select source file");
			File sourceFile = fileChooser.showOpenDialog(stage);
			model.setSourceFile(sourceFile);
		});

		btnSourceFile.textProperty().bindBidirectional(model.sourceFileProperty(), new FileToStringConverter());
        BooleanBinding isMandatoryFieldsEmpty = model.sourceFileProperty().isNull();

        // init c14n combo box
        cbXmlC14nMethod.valueProperty().bindBidirectional(model.xmlC14nMethodProperty());
        cbXmlC14nMethod.getItems().setAll(XmlC14nMethod.values());

        // add listener to c14n combo box value and update adjacent label with its uri value
        cbXmlC14nMethod.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    lblMethodUri.setText(newValue.getUri());
                }
        );
        cbXmlC14nMethod.setValue(XmlC14nMethod.C14N_NONE);


        // init calculation button
		btnCalculate.disableProperty().bind(isMandatoryFieldsEmpty);

		// TODO: needs to go to the model
		btnCalculate.setOnAction(event -> {

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
