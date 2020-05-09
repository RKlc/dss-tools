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

		// bind the file name and the XML info to their respective labels
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

        // listens to c14n combo box value and updates adjacent label with its URI
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

        /*
         * SaveAs button
         */

        btnSaveAs.setDisable(true);

        // listener to enable button if any valid transformation method is selected
        cbMethod.getSelectionModel().selectedItemProperty().addListener((options, oldMethod, newMethod) -> {

            if (newMethod != null && newMethod != XmlC14nMethod.C14N_NONE) {
                btnSaveAs.setDisable(false);
            } else {
                btnSaveAs.setDisable(true);
            }
        });

        /*
         * Table for digest messages
         */

        // bind columns to data class
        colAlgorithm.setCellValueFactory(
                new PropertyValueFactory<DigestData,String>("algorithm")
        );
        colDigest.setCellValueFactory(
                new PropertyValueFactory<DigestData,String>("digest")
        );

        // digest messages have editable cells
        colDigest.setCellFactory(TextFieldTableCell.forTableColumn());

        // set all table cells to selectable
        /*
        tblDigest.getSelectionModel().setCellSelectionEnabled(true);
        tblDigest.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        */

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

			if (cbMethod != null && cbMethod.getValue() != XmlC14nMethod.C14N_NONE) {
                try {
                    org.apache.xml.security.Init.init();
                    Canonicalizer c14n = Canonicalizer.getInstance(cbMethod.getValue().getUri());
                    outArray = c14n.canonicalize(sourceArray);
                } catch (Exception e) {
                    LOG.error("Cannot canonicalize the binaries", e);
                    Alert alert = new Alert(AlertType.ERROR, "Unable to transform file: " + e.getMessage(), ButtonType.CLOSE);
                    alert.showAndWait();
                    outArray = null;
                }
            } else {
			    outArray =null;
            }

			if (outArray != null) {
                updateDigest(outArray);

            } else if (sourceArray != null) {
                updateDigest(sourceArray);
            } else {
                LOG.info("Nothing to calculate, source file is empty.");
            }

            if (outArray != null) {
                try {
                    FileUtils.writeByteArrayToFile(new File("D:\\out.xml"), outArray);
                } catch (IOException e) {
                    LOG.error("Unable to save file", e);
                    Alert alert = new Alert(AlertType.ERROR, "Unable to save file: " + e.getMessage(), ButtonType.CLOSE);
                    alert.showAndWait();
                }
            }
		});

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
     * Update list of message digests in text area.
     */
    private void updateDigest(byte[] message) {

        MessageDigest md;
        StringBuffer content = new StringBuffer("");
        byte[] digest;
        StringBuffer hexString;

        ArrayList<DigestData> list = new ArrayList<>();

        for (DigestAlgorithm algorithm : DigestAlgorithm.values()) {
            try {
                md = algorithm.getMessageDigest();

                //Passing data to the created MessageDigest Object
                md.update(message);

                //Compute the message digest
                digest = md.digest();

                //Converting the byte array in to HexString format
                hexString = new StringBuffer();

                for (int i = 0; i<digest.length; i++) {
                    hexString.append(Integer.toHexString(0xFF & digest[i]));
                }

                list.add(new DigestData(algorithm.getJavaName(), hexString.toString()));

            } catch (NoSuchAlgorithmException e) {

                list.add(new DigestData(algorithm.getJavaName(), resources.getString("AlgorithmNotSupported")));
                LOG.debug(algorithm.getJavaName() + " algorithm not provided.");
            }
        }

        if (list != null) {
            final ObservableList<DigestData> data = FXCollections.observableArrayList(list);
            tblDigest.setItems(data);
        } else {
            LOG.debug("Digest Message did not contain any data.");
        }
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