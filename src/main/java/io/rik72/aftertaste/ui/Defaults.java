package io.rik72.aftertaste.ui;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Defaults {
	// MUST be final (why??)
	public static final int 	 WINDOW_WIDTH = 1024;
	public static final int 	 WINDOW_HEIGHT = 768;

	public static Font   FONT_NORMAL = Font.font("VT323", 20);
	public static Font   FONT_HILIGHT = Font.font("VT323", FontWeight.BOLD, 20);
	public static Font   FONT_EMPHASIS = Font.font("VT323", FontPosture.ITALIC, 20);

	public static String COLOR_TEXT_FLOW_NORMAL 			= "gray";
	public static String COLOR_TEXT_FLOW_HILIGHT 			= "#FFFFFF";
	public static String COLOR_TEXT_FLOW_EMPHASIS 			= "green";

	public static String COLOR_TEXT_FLOW_BG 				= "#0D0D0D";//"derive(saddlebrown, -60%)";
	public static String COLOR_TEXT_FLOW_SCROLLBAR 			= "gray";
	public static String COLOR_MENU_BG 						= "#181818";
	public static String COLOR_MENU_HILIGHT 				= "#404040";
	public static String COLOR_MENU_SEPARATOR 				= "#404040";

	public static String COLOR_WINDOWS_BG 					= "#000000";//"derive(saddlebrown, -70%)";
	public static String COLOR_WINDOWS_TEXT 				= "#E0E0E0";
	public static String COLOR_WINDOWS_LOCATION_IMAGE_BG 	= "#000000";//"derive(saddlebrown, -80%)";
	public static String COLOR_WINDOWS_BUTTON 				= "#808080";
	public static String COLOR_WINDOWS_HOVER 				= "#A0A0A0";
	public static String COLOR_WINDOWS_MODAL_BG 			= "#101010";
	public static String COLOR_WINDOWS_MODAL_BORDER 		= "#606060";
}
