package sk.isdd.validator.fx;

import java.io.File;
import javafx.util.StringConverter;

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
			return "Select file...";
		}
		return object.getAbsolutePath();
	}

	@Override
	public File fromString(String string) {
		return null;
	}

}
