package info.seravee.wallmanager.ui.commons;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.LoggerFactory;

public class I18N {
	private static final Object lock = new Object();
	private static final String NO_TRANSLATION_FOUND = "??";
	private static final Set<String> warnedMissingKeys = new HashSet<String>();

	private static final String YES_STRING_TRANSLATION = "bool.yes";
	private static final String NO_STRING_TRANSLATION = "bool.no";

	private static ResourceBundle BUNDLE;

	private I18N() {/**/}

	public static ResourceBundle getBundle() {
		synchronized (lock) {
			if (BUNDLE == null) {
				BUNDLE = ResourceBundle.getBundle("lang/messages", Locale.getDefault());
			}
		}
		return BUNDLE;
	}
	
	public static String get(boolean boolVal) {
		return get(boolVal ? YES_STRING_TRANSLATION : NO_STRING_TRANSLATION);
	}
	
	public static String get(Enum<?> enumVal) {
		if (enumVal == null) {
			return null;
		}
		String translationKey = enumVal.getClass().getName() + "." + enumVal.toString();
		return get(translationKey);
	}

	public static String get(String strName) {
		return get(strName, true);
	}

	public static String get(String strName, boolean defaultMessage) {
		String msg = null;

		if (strName == null) {
			return "";
		}

		try {
			return getBundle().getString(strName);
		} catch (final MissingResourceException mre) {

			// We log warn only once
			if (!warnedMissingKeys.contains(strName)) {

				LoggerFactory.getLogger(I18N.class)
						.warn("translation not found for " + strName + " for Locale " + Locale.getDefault());

				warnedMissingKeys.add(strName);
			}

			if (defaultMessage)
				msg = NO_TRANSLATION_FOUND + strName + NO_TRANSLATION_FOUND;
		}

		return msg;
	}

	public static String formatMessage(String strName, Object... args) {
		return String.format(get(strName), args);
	}
}
