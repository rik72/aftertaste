package io.rik72.vati.locale;

import java.util.Locale;
import java.util.ResourceBundle;

import io.rik72.vati.core.VatiLocale;

public class LocaleBackend {

	public void setLocale(Locale locale) {
		setBundle(getResourceBundle(locale));
	}

	ResourceBundle getBundle() {
		return bundle;
	}

	public static LocaleBackend get() {
		if (instance == null)
			instance = new LocaleBackend();
		return instance;
	}

	///////////////////////////////////////////////////////////////////////////
	private static final String BUNDLE_BASE_NAME = "l10n/strings";

	private ResourceBundle bundle;

	private LocaleBackend() {
		setBundle(getResourceBundle(VatiLocale.getDefault().getLocale()));
	}

	private void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	private static LocaleBackend instance;

	private static ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
	}
}
