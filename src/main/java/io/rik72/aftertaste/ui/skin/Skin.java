package io.rik72.aftertaste.ui.skin;

public enum Skin {

	CUSTOM (
		"Custom",
		null),

	NIGHT (
		"Night",
		new SkinData(
			AftertasteFont.VT323,
			20,
			"gray",
			"#FFFFFF",
			"green",
			"#0D0D0D",
			"gray",
			"#090909;",
			"#404040",
			"#404040",
			"#090909",
			"#B0B0B0",
			"#000000",
			"#505050",
			"#808080",
			"#101010",
			"#606060")),
		
	WOOD (
		"Wood",
		new SkinData(
			AftertasteFont.VT323,
			20,
			"derive(saddlebrown, 60%)",
			"#FFFFFF",
			"green",
			"derive(saddlebrown, -60%)",
			"derive(saddlebrown, 60%)",
			"derive(saddlebrown, -80%)",
			"#404040",
			"#404040",
			"derive(saddlebrown, -70%)",
			"#C0C0C0",
			"derive(saddlebrown, -80%)",
			"#505050",
			"#808080",
			"#101010",
			"#606060")
	);

	public String name;
	public SkinData data;

	public static Skin getDefaultSkin() {
		return Skin.NIGHT;
	}

	private Skin(String name, SkinData data) {
		this.name = name;
		this.data = data;
	}
}
