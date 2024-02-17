package io.rik72.aftertaste.ui.skin;

public class SkinData {
	public AftertasteFont font;
	public Integer fontSize;

	public String COLOR_TEXT_FLOW_NORMAL;
	public String COLOR_TEXT_FLOW_HILIGHT;
	public String COLOR_TEXT_FLOW_EMPHASIS;
	public String COLOR_TEXT_FLOW_BG;
	public String COLOR_TEXT_FLOW_SCROLLBAR;
	public String COLOR_MENU_BG;
	public String COLOR_MENU_HILIGHT;
	public String COLOR_MENU_SEPARATOR;
	public String COLOR_WINDOW_BG;
	public String COLOR_WINDOW_TEXT;
	public String COLOR_WINDOW_LOCATION_IMAGE_BG;
	public String COLOR_WINDOW_BUTTON;
	public String COLOR_WINDOW_HOVER;
	public String COLOR_WINDOW_MODAL_BG;
	public String COLOR_WINDOW_MODAL_BORDER;

	public SkinData(
		          AftertasteFont font,
				  Integer fontSize,
				  String COLOR_TEXT_FLOW_NORMAL,
				  String COLOR_TEXT_FLOW_HILIGHT,
				  String COLOR_TEXT_FLOW_EMPHASIS,
				  String COLOR_TEXT_FLOW_BG,
				  String COLOR_TEXT_FLOW_SCROLLBAR,
				  String COLOR_MENU_BG,
				  String COLOR_MENU_HILIGHT,
				  String COLOR_MENU_SEPARATOR,
				  String COLOR_WINDOW_BG,
				  String COLOR_WINDOW_TEXT,
				  String COLOR_WINDOW_LOCATION_IMAGE_BG,
				  String COLOR_WINDOW_BUTTON,
				  String COLOR_WINDOW_HOVER,
				  String COLOR_WINDOW_MODAL_BG,
				  String COLOR_WINDOW_MODAL_BORDER) {
		this.font = font;
		this.fontSize = fontSize;
		this.COLOR_TEXT_FLOW_NORMAL = COLOR_TEXT_FLOW_NORMAL;
		this.COLOR_TEXT_FLOW_HILIGHT = COLOR_TEXT_FLOW_HILIGHT;
		this.COLOR_TEXT_FLOW_EMPHASIS = COLOR_TEXT_FLOW_EMPHASIS;
		this.COLOR_TEXT_FLOW_BG = COLOR_TEXT_FLOW_BG;
		this.COLOR_TEXT_FLOW_SCROLLBAR = COLOR_TEXT_FLOW_SCROLLBAR;
		this.COLOR_MENU_BG = COLOR_MENU_BG;
		this.COLOR_MENU_HILIGHT = COLOR_MENU_HILIGHT;
		this.COLOR_MENU_SEPARATOR = COLOR_MENU_SEPARATOR;
		this.COLOR_WINDOW_BG = COLOR_WINDOW_BG;
		this.COLOR_WINDOW_TEXT = COLOR_WINDOW_TEXT;
		this.COLOR_WINDOW_LOCATION_IMAGE_BG = COLOR_WINDOW_LOCATION_IMAGE_BG;
		this.COLOR_WINDOW_BUTTON = COLOR_WINDOW_BUTTON;
		this.COLOR_WINDOW_HOVER = COLOR_WINDOW_HOVER;
		this.COLOR_WINDOW_MODAL_BG = COLOR_WINDOW_MODAL_BG;
		this.COLOR_WINDOW_MODAL_BORDER = COLOR_WINDOW_MODAL_BORDER;
	}

	public SkinData(SkinData skinData) {
		this.font = skinData.font;
		this.fontSize = skinData.fontSize;
		this.COLOR_TEXT_FLOW_NORMAL = skinData.COLOR_TEXT_FLOW_NORMAL;
		this.COLOR_TEXT_FLOW_HILIGHT = skinData.COLOR_TEXT_FLOW_HILIGHT;
		this.COLOR_TEXT_FLOW_EMPHASIS = skinData.COLOR_TEXT_FLOW_EMPHASIS;
		this.COLOR_TEXT_FLOW_BG = skinData.COLOR_TEXT_FLOW_BG;
		this.COLOR_TEXT_FLOW_SCROLLBAR = skinData.COLOR_TEXT_FLOW_SCROLLBAR;
		this.COLOR_MENU_BG = skinData.COLOR_MENU_BG;
		this.COLOR_MENU_HILIGHT = skinData.COLOR_MENU_HILIGHT;
		this.COLOR_MENU_SEPARATOR = skinData.COLOR_MENU_SEPARATOR;
		this.COLOR_WINDOW_BG = skinData.COLOR_WINDOW_BG;
		this.COLOR_WINDOW_TEXT = skinData.COLOR_WINDOW_TEXT;
		this.COLOR_WINDOW_LOCATION_IMAGE_BG = skinData.COLOR_WINDOW_LOCATION_IMAGE_BG;
		this.COLOR_WINDOW_BUTTON = skinData.COLOR_WINDOW_BUTTON;
		this.COLOR_WINDOW_HOVER = skinData.COLOR_WINDOW_HOVER;
		this.COLOR_WINDOW_MODAL_BG = skinData.COLOR_WINDOW_MODAL_BG;
		this.COLOR_WINDOW_MODAL_BORDER = skinData.COLOR_WINDOW_MODAL_BORDER;
	}

	public void applyOverrides(SkinData overrides) {
		this.font = (AftertasteFont) elvis(overrides.font, this.font);
		this.fontSize = (Integer) elvis(overrides.fontSize, this.fontSize);
		this.COLOR_TEXT_FLOW_NORMAL = (String) elvis(overrides.COLOR_TEXT_FLOW_NORMAL, this.COLOR_TEXT_FLOW_NORMAL);
		this.COLOR_TEXT_FLOW_HILIGHT = (String) elvis(overrides.COLOR_TEXT_FLOW_HILIGHT, this.COLOR_TEXT_FLOW_HILIGHT);
		this.COLOR_TEXT_FLOW_EMPHASIS = (String) elvis(overrides.COLOR_TEXT_FLOW_EMPHASIS, this.COLOR_TEXT_FLOW_EMPHASIS);
		this.COLOR_TEXT_FLOW_BG = (String) elvis(overrides.COLOR_TEXT_FLOW_BG, this.COLOR_TEXT_FLOW_BG);
		this.COLOR_TEXT_FLOW_SCROLLBAR = (String) elvis(overrides.COLOR_TEXT_FLOW_SCROLLBAR, this.COLOR_TEXT_FLOW_SCROLLBAR);
		this.COLOR_MENU_BG = (String) elvis(overrides.COLOR_MENU_BG, this.COLOR_MENU_BG);
		this.COLOR_MENU_HILIGHT = (String) elvis(overrides.COLOR_MENU_HILIGHT, this.COLOR_MENU_HILIGHT);
		this.COLOR_MENU_SEPARATOR = (String) elvis(overrides.COLOR_MENU_SEPARATOR, this.COLOR_MENU_SEPARATOR);
		this.COLOR_WINDOW_BG = (String) elvis(overrides.COLOR_WINDOW_BG, this.COLOR_WINDOW_BG);
		this.COLOR_WINDOW_TEXT = (String) elvis(overrides.COLOR_WINDOW_TEXT, this.COLOR_WINDOW_TEXT);
		this.COLOR_WINDOW_LOCATION_IMAGE_BG = (String) elvis(overrides.COLOR_WINDOW_LOCATION_IMAGE_BG, this.COLOR_WINDOW_LOCATION_IMAGE_BG);
		this.COLOR_WINDOW_BUTTON = (String) elvis(overrides.COLOR_WINDOW_BUTTON, this.COLOR_WINDOW_BUTTON);
		this.COLOR_WINDOW_HOVER = (String) elvis(overrides.COLOR_WINDOW_HOVER, this.COLOR_WINDOW_HOVER);
		this.COLOR_WINDOW_MODAL_BG = (String) elvis(overrides.COLOR_WINDOW_MODAL_BG, this.COLOR_WINDOW_MODAL_BG);
		this.COLOR_WINDOW_MODAL_BORDER = (String) elvis(overrides.COLOR_WINDOW_MODAL_BORDER, this.COLOR_WINDOW_MODAL_BORDER);
	}

	private Object elvis(Object curl, Object eyes) {
		return curl != null ? curl : eyes;
	}

	public boolean check() {
		return
			font != null &&
			fontSize != null && fontSize > 0 &&
			COLOR_TEXT_FLOW_NORMAL != null && COLOR_TEXT_FLOW_NORMAL.length() > 0 &&
			COLOR_TEXT_FLOW_HILIGHT != null && COLOR_TEXT_FLOW_HILIGHT.length() > 0 &&
			COLOR_TEXT_FLOW_EMPHASIS != null && COLOR_TEXT_FLOW_EMPHASIS.length() > 0 &&
			COLOR_TEXT_FLOW_BG != null && COLOR_TEXT_FLOW_BG.length() > 0 &&
			COLOR_TEXT_FLOW_SCROLLBAR != null && COLOR_TEXT_FLOW_SCROLLBAR.length() > 0 &&
			COLOR_MENU_BG != null && COLOR_MENU_BG.length() > 0 &&
			COLOR_MENU_HILIGHT != null && COLOR_MENU_HILIGHT.length() > 0 &&
			COLOR_MENU_SEPARATOR != null && COLOR_MENU_SEPARATOR.length() > 0 &&
			COLOR_WINDOW_BG != null && COLOR_WINDOW_BG.length() > 0 &&
			COLOR_WINDOW_TEXT != null && COLOR_WINDOW_TEXT.length() > 0 &&
			COLOR_WINDOW_LOCATION_IMAGE_BG != null && COLOR_WINDOW_LOCATION_IMAGE_BG.length() > 0 &&
			COLOR_WINDOW_BUTTON != null && COLOR_WINDOW_BUTTON.length() > 0 &&
			COLOR_WINDOW_HOVER != null && COLOR_WINDOW_HOVER.length() > 0 &&
			COLOR_WINDOW_MODAL_BG != null && COLOR_WINDOW_MODAL_BG.length() > 0 &&
			COLOR_WINDOW_MODAL_BORDER != null && COLOR_WINDOW_MODAL_BORDER.length() > 0;
	}

}
