package sk.isdd.validator.fx;

import javafx.util.StringConverter;

import java.io.File;

/**
 * Helper for File name conversion to string.
 * <p>
 * Used for bidirectional bindings between File object and visual FX controls (like buttons).
 * Supports default string message if File is not selected.
 */
public class FileToStringConverter extends StringConverter<File> {

	@Override
	public String toString(File object) {
		if (object == null){
			return I18nMsg.getString("emptySelectFile");
		}
		return object.getAbsolutePath();
	}

	@Override
	public File fromString(String string) {
		return null;
	}

}
