package io.rik72.aftertaste.ui.skin;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CurrentSkin {

	public static Skin skin;
	public static SkinData data = Skin.getDefaultSkin().data;

	public static final Font FONT_NORMAL = Font.font("VT323", 20);
	public static final Font FONT_HILIGHT = Font.font("VT323", FontWeight.BOLD, 20);
	public static final Font FONT_EMPHASIS = Font.font("VT323", FontPosture.ITALIC, 20);

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
}
