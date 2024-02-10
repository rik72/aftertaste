package io.rik72.aftertaste.ui.views.menu;

import java.net.URL;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.config.Config;
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
import javafx.stage.Stage;

public class AftertasteAbout extends MenuItem {
	public AftertasteAbout(String text) {
		setText(text);
		setOnAction(e -> {
			String version = Config.get().get("application.version");
			Stage modal = ViewHelper.createModal(420, 180, 30);
			URL logoURL = getClass().getClassLoader().getResource("images/Aftertaste.png");
			ImageView logo = new ImageView(new Image(logoURL.toString(), 64, 64, true, true));
			VBox vBox = (VBox)modal.getScene().getRoot();
			HBox topHBox = new HBox();
			topHBox.getChildren().addAll(logo, new Text("Aftertaste v" + version));
			topHBox.setSpacing(10);
			topHBox.setAlignment(Pos.CENTER_LEFT);
			TextFlow description = new TextFlow();
			Hyperlink licenseHL = new Hyperlink("GPL-3.0 license");
			licenseHL.setOnAction(ev -> {
				App.openURL("https://github.com/rik72/aftertaste#GPL-3.0-1-ov-file");
			});
			Hyperlink githubHL = new Hyperlink("Aftertaste on github");
			githubHL.setOnAction(ev -> {
				App.openURL("https://github.com/rik72/aftertaste");
			});
			description.getChildren().addAll(
				new Text("Aftertaste is an open source project under the "), licenseHL, new Text(".\n"),
				new Text("See "), githubHL, new Text(".")
			);
			vBox.getChildren().addAll(topHBox, description);
			vBox.setSpacing(10);
			vBox.requestFocus();
			modal.show();
		});		
	}
}
