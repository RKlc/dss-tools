package sk.isdd.validator.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.DigestAlgorithm;
import sk.isdd.validator.enumerations.XmlC14nMethod;
import sk.isdd.validator.model.DigestData;
import sk.isdd.validator.xml.XmlFile;
import sk.isdd.validator.xml.XmlFileChooser;
import sk.isdd.validator.xml.XmlFileToStringConverter;
import sk.isdd.validator.xml.XmlFileToInfoConverter;
import sk.isdd.validator.model.DigestModel;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for message digests.
 *
 * <p> User can select source file and optional c14n canonical transformation.
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
     * Access to translations.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Button to select source file for processing.
     */
    @FXML
    private Button btnSourceFile;

    /**
     * Label with info about state of the source file.
     */
    @FXML
    public Label lblSourceFileInfo;

    /**
     * Combo box for selecting canonicalization method (enumerated in {@link sk.isdd.validator.enumerations.XmlC14nMethod}).
     */
	@FXML
	public ComboBox<XmlC14nMethod> cbMethod;

    /**
     * Label holding information about W3C URI specification from selected enumeration.
     */
    @FXML
    public Label lblMethodUri;

    /**
     * Calculation button starts the processing task for message digest.
     */
	@FXML
	private Button btnCalculate;

    /**
     * Button for saving canonicalized XML file to drive.
     */
	@FXML
	private Button btnSaveAs;

    @FXML
    private TableView<DigestData> tblDigest;

    @FXML
    private TableColumn<DigestData, String> colAlgorithm;

    @FXML
    private TableColumn<DigestData, String> colDigest;

    /**
     * Stage (window) used for this controller.
     */
	private Stage stage;

    /**
     * Data model and data processing used with this controller.
     */
	private DigestModel model;

    /**
     * Customized instance of file open dialog.
     */
    private XmlFileChooser xmlFileChooser;

    /**
     * Initialization of this controller for message digest processing (auto invoked).
     *
     * <p> It has access to all the @FXML annotated resources, constructor does not.
     * However there si no access to the {@code this.stage} and as such cannot be used for output.
     */
    @FXML
	public void initialize() {

		model = new DigestModel();
        xmlFileChooser = new XmlFileChooser();

        /*
         * Source file button
         */

        // show custom file chooser when the button is pressed
		btnSourceFile.setOnAction(event -> {
            model.setSourceFile(xmlFileChooser.showXmlOpenDialog(stage));
		});

		// bind the file name and information about the file to their respective labels
		btnSourceFile.textProperty().bindBidirectional(model.sourceFileProperty(), new XmlFileToStringConverter());
        lblSourceFileInfo.textProperty().bindBidirectional(model.sourceFileProperty(), new XmlFileToInfoConverter());

        BooleanBinding isSourceFileEmpty = model.sourceFileProperty().isNull();

        /*
         * Canonicalization combo box
         */

        // bind c14n combo box to the appropriate data model property
        cbMethod.valueProperty().bindBidirectional(model.methodProperty());

        // turn off the c14n combo box by default
        updateMethod(null);

        // listen if the source file is changed and adjust combo box selection list accordingly
        model.sourceFileProperty().addListener((options, oldSourceFile, newSourceFile) ->{
            updateMethod(newSourceFile);
        });

        // listen to c14n combo box value and update adjacent label with its URI
        cbMethod.getSelectionModel().selectedItemProperty().addListener((options, oldMethod, newMethod) -> {

            if (newMethod != null) {
                lblMethodUri.setText(newMethod.getUri());
            }
        });

        /*
         * Calculate button
         */

        // calculation of message digests are allowed on any selected source file
		btnCalculate.disableProperty().bind(isSourceFileEmpty);

		// just calculate data, they are observed and will auto-update
        btnCalculate.setOnAction(event -> {
            model.calculateDigestData();
        });

        /*
         * SaveAs button
         */

        btnSaveAs.setDisable(true);

        // listener to enable SaveAs button if any valid transformation method is selected
        cbMethod.getSelectionModel().selectedItemProperty().addListener((options, oldMethod, newMethod) -> {

            if (newMethod != null && newMethod != XmlC14nMethod.C14N_NONE) {
                btnSaveAs.setDisable(false);
            } else {
                btnSaveAs.setDisable(true);
            }
        });

        // Save transformation as new file
        btnSaveAs.setOnAction(event -> {
            // TODO: open save as dialog and save transformed content
/*
            if (outArray != null) {
                try {
                    FileUtils.writeByteArrayToFile(new File("D:\\out.xml"), outArray);
                } catch (IOException e) {
                    LOG.error("Unable to save file", e);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file: " + e.getMessage(), ButtonType.CLOSE);
                    alert.showAndWait();
                }
            }
            Alert alert = new Alert(AlertType.CONFIRMATION, "Loaded");
            alert.showAndWait();

*/
        });

        /*
         * Table for digest messages
         */

        // bind columns to data class
        colAlgorithm.setCellValueFactory(
                new PropertyValueFactory<>("algorithmName")
        );
        colDigest.setCellValueFactory(
                new PropertyValueFactory<>("digestBase64")
        );

        // editable cells for digest messages (double click to copy text out of them)
        colDigest.setCellFactory(TextFieldTableCell.forTableColumn());

        // bind observation of data source
        tblDigest.setItems(model.getDigestList());

        /*
        // TODO: copy-paste feature: selections need support for copy-paste handler first
        // set all table cells to be selectable
        tblDigest.getSelectionModel().setCellSelectionEnabled(true);
        tblDigest.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        */

		LOG.debug("DigestController is initialized.");
	}

    /**
     * Update "c14n method" combo box to reflect type of the selected source file.
     *
     * If well formed xml file was loaded, then c14n transformations are allowed.
     *
     * @param xmlFile the file whose state is reflected upon combo box
     */
    private void updateMethod(XmlFile xmlFile) {
        cbMethod.getItems().removeAll(cbMethod.getItems());

        // selecting c14n method is allowed only for well formed XML document
        if (xmlFile != null && xmlFile.isXmlDocument()) {

            cbMethod.getItems().setAll(XmlC14nMethod.values());
            cbMethod.setDisable(false);

        } else {
            cbMethod.getItems().setAll(XmlC14nMethod.C14N_NONE);
            cbMethod.setDisable(true);
        };
        cbMethod.setValue(XmlC14nMethod.C14N_NONE);
    }

    /**
     * Setting stage (window) passed down from the creator of this controller.
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