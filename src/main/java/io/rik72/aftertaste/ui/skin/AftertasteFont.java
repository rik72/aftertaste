package io.rik72.aftertaste.ui.skin;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public enum AftertasteFont {
	VT323				("VT323"),
	GEORGIA				("Georgia"),
	PRESS_START_2P		("Press Start 2P"),
	PIXELIFY_SANS		("Pixelify Sans"),
	;

	private String family;

	private AftertasteFont(String family) {
		this.family = family;
	}

	public Font getNormal(int size) {
		return Font.font(family, size);
	}

	public Font getHilight(int size) {
		return Font.font(family, FontWeight.BOLD, size);
	}

	public Font getEmphasis(int size) {
		return Font.font(family, FontPosture.ITALIC, size);
	}
}
