package sk.isdd.validator.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.isdd.validator.enumerations.XmlC14nMethod;

import java.io.File;


public class DigestModel {

	private static final Logger LOG = LoggerFactory.getLogger(DigestModel.class);

	private ObjectProperty<File> sourceFile = new SimpleObjectProperty<>();
    private ObjectProperty<XmlC14nMethod> method = new SimpleObjectProperty<>();

	public File getSourceFile() {
		return sourceFile.get();
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile.set(sourceFile);
	}

	public ObjectProperty<File> sourceFileProperty() {
		return sourceFile;
	}

    public ObjectProperty<XmlC14nMethod> methodProperty() {
        return method;
    }

}
