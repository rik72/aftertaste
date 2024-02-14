package io.rik72.aftertaste.ui.views.menu;

import java.net.URL;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.config.Config;
import io.rik72.aftertaste.ui.skin.CurrentSkin;
import io.rik72.aftertaste.ui.views.AlertModal;
import io.rik72.aftertaste.ui.views.ViewHelper;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AftertasteAbout extends MenuItem {
	public AftertasteAbout(String text) {
		setText(text);
		setOnAction(e -> {
			String version = Config.get().get("application.version");
			AlertModal modal = ViewHelper.createModal(540, 260, 30);
			URL logoURL = getClass().getClassLoader().getResource("images/Aftertaste.png");
			ImageView logo = new ImageView(new Image(logoURL.toString(), 64, 64, true, true));
			VBox vBox = modal.getVBox();
			HBox topHBox = new HBox();
			Text versionText = new Text("Aftertaste v" + version);
			versionText.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			versionText.setFont(CurrentSkin.FONT_NORMAL);
			topHBox.getChildren().addAll(logo, versionText);
			topHBox.setSpacing(10);
			topHBox.setAlignment(Pos.CENTER_LEFT);
			TextFlow description = new TextFlow();
			Hyperlink licenseHL = new Hyperlink("GPL-3.0 license");
			licenseHL.setFont(CurrentSkin.FONT_NORMAL);
			licenseHL.setOnAction(ev -> {
				App.openURL("https://github.com/rik72/aftertaste#GPL-3.0-1-ov-file");
			});
			Hyperlink githubHL = new Hyperlink("Aftertaste on github");
			githubHL.setFont(CurrentSkin.FONT_NORMAL);
			githubHL.setOnAction(ev -> {
				App.openURL("https://github.com/rik72/aftertaste");
			});
			Text t1 = new Text("Aftertaste is an open source project under the ");
			t1.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			t1.setFont(CurrentSkin.FONT_NORMAL);
			Text t2 = new Text(".\n");
			t2.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			t2.setFont(CurrentSkin.FONT_NORMAL);
			Text t3 = new Text("See ");
			t3.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			t3.setFont(CurrentSkin.FONT_NORMAL);
			Text t4 = new Text(".\n");
			t4.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			t4.setFont(CurrentSkin.FONT_NORMAL);
			Hyperlink attribHL = new Hyperlink("Image on main screen by tirachard");
			attribHL.setFont(CurrentSkin.FONT_NORMAL);
			attribHL.setOnAction(ev -> {
				App.openURL("https://www.freepik.com/free-photo/black-marble-patterned-texture-background-marble-thailand-abstract-natural-marble-black-white-design_1174373.htm#query=black%20texture&position=2&from_view=keyword&track=ais&uuid=e5dcdb0b-fe2d-462c-93ca-514de6205f66");
			});
			Text t5 = new Text(" on Freepik.");
			t5.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
			t5.setFont(CurrentSkin.FONT_NORMAL);
			description.getChildren().addAll(
				t1, licenseHL, t2, t3, githubHL, t4, attribHL, t5);
			vBox.getChildren().addAll(topHBox, description);
			vBox.setSpacing(10);
			vBox.requestFocus();
			modal.show();
		});		
	}
}
