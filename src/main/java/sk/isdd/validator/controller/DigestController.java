package sk.isdd.validator.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.fx.FileToStringConverter;
import sk.isdd.validator.fx.I18nMsg;
import sk.isdd.validator.model.DigestModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class DigestController {

	private static final Logger LOG = LoggerFactory.getLogger(DigestController.class);

    /**
     * Combo box for selecting canonicalization method (enumerated in {@link sk.isdd.validator.enumerations.XmlC14nMethod}).
     */
	@FXML
	public ComboBox<XmlC14nMethod> cbXmlC14nMethod;

    /**
     * Label holding information about W3C URI specification from selected enumeration.
     */
    @FXML
    public Label lblMethodUri;

    /**
     * Label with info about state of the source file.
     */
    @FXML
    public Label lblSourceFileInfo;

    /**
     * Button to select source file for processing.
     */
    @FXML
	private Button btnSourceFile;

    /**
     * Calculation button sets the processing task for message digest.
     */
	@FXML
	private Button btnCalculate;

    /**
     * Button for saving canonicalized XML file to drive.
     */
	@FXML
	private Button btnSaveAs;

    @FXML
    private ResourceBundle lang;

    /**
     * Stage (window) used for this controller.
     */
	private Stage stage;

    /**
     * Data model and data processing used with this controller.
     */
	private DigestModel model;

    /**
     * Initialization of this controller for message digest processing (auto invoked).
     * <p>
     * It has access to all the @FXML annotated resources, constructor does not.
     * However there si no access to the {@code this.stage} and as such cannot be used for output.
     */
    @FXML
	public void initialize() {
		model = new DigestModel();

        // init loading button for source file
		btnSourceFile.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(I18nMsg.getString("titleSelectSourceFile"));
			File sourceFile = fileChooser.showOpenDialog(stage);
			model.setSourceFile(sourceFile);
		});

		// bind its text label to filename
		btnSourceFile.textProperty().bindBidirectional(model.sourceFileProperty(), new FileToStringConverter());
        BooleanBinding isSourceFileEmpty = model.sourceFileProperty().isNull();

        // init c14n combo box
        // TODO: limit combo box values depending on type of the source file
        cbXmlC14nMethod.valueProperty().bindBidirectional(model.xmlC14nMethodProperty());
        cbXmlC14nMethod.getItems().setAll(XmlC14nMethod.values());

        // add listener to c14n combo box value to update adjacent label with its URI value
        cbXmlC14nMethod.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    lblMethodUri.setText(newValue.getUri());
                }
        );
        cbXmlC14nMethod.setValue(XmlC14nMethod.C14N_NONE);


        // init calculation button
		btnCalculate.disableProperty().bind(isSourceFileEmpty);

		// TODO: needs to go to the model
		btnCalculate.setOnAction(event -> {

			File sourceFile = model.getSourceFile();

			//init array with file length
			byte[] sourceArray;

			try {
                sourceArray = JavaUtils.getBytesFromFile(sourceFile.getAbsolutePath());
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

    /**
     * Setting stage (window) passed down from teh creator of this controller.
     *
     * @param stage the window dedicated to this controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage()
    {
        return stage;
    }

}