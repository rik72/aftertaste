package io.rik72.aftertaste.ui.skin;

public enum Skin {

	CUSTOM (
		"Custom",
		null),

	NIGHT (
		"Night",
		new SkinData(
		"gray",
		"#FFFFFF",
		"green",
		"#0D0D0D",
		"gray",
		"#181818",
		"#404040",
		"#404040",
		"#000000",
		"#E0E0E0",
		"#000000",
		"#808080",
		"#A0A0A0",
		"#101010",
		"#606060")),
		
	WOOD (
		"Wood",
		new SkinData(
		"derive(saddlebrown, 60%)",
		"#FFFFFF",
		"green",
		"derive(saddlebrown, -60%)",
		"derive(saddlebrown, 60%)",
		"derive(saddlebrown, -80%)",
		"#404040",
		"#404040",
		"derive(saddlebrown, -70%)",
		"#E0E0E0",
		"derive(saddlebrown, -80%)",
		"#808080",
		"#A0A0A0",
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
