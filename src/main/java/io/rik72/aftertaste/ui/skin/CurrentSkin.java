package io.rik72.aftertaste.ui.skin;

import javafx.scene.text.Font;

public class CurrentSkin {

	public static Skin skin;
	public static SkinData data = Skin.getDefaultSkin().data;

	static {
		setSkin(Skin.getDefaultSkin());
	}

	public static void setSkin(Skin skin) {
		CurrentSkin.skin = skin;
		CurrentSkin.data = skin.data;
	}

	public static void setSkin(Skin skin, SkinData data) {
		CurrentSkin.skin = skin;
		if (skin.data != null) {
			CurrentSkin.data = new SkinData(skin.data);
			CurrentSkin.data.applyOverrides(data);
		}
		else {
			CurrentSkin.data = data;
		}
	}

	public static Font getGUIFont() {
		return AftertasteFont.VT323.getNormal(20);
	}

	public static Font getTerminalFontNormal() {
		return data.font.getNormal(data.fontSize);
	}
	public static Font getTerminalFontHilight() {
		return data.font.getHilight(data.fontSize);
	}
	public static Font getTerminalFontEmphasis() {
		return data.font.getEmphasis(data.fontSize);
	}

}
