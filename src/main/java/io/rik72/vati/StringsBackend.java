package io.rik72.vati;

import java.util.Locale;
import java.util.ResourceBundle;

class StringsBackend {

	void setLocale(Locale locale) {
		setBundle(getResourceBundle(locale));
	}

	ResourceBundle getBundle() {
		return bundle;
	}

	static StringsBackend get() {
		return instance;
	}

	///////////////////////////////////////////////////////////////////////////
	private static final String BUNDLE_BASE_NAME = "l10n/strings";

	private ResourceBundle bundle;

	private StringsBackend() {
		setBundle(getResourceBundle(VatiLocale.getDefault().getLocale()));
	}

	private void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	private static StringsBackend instance = new StringsBackend();

	private static ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
	}
}
