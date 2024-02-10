package io.rik72.aftertaste.ui.views.menu;

import java.util.Map.Entry;

import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.BrewController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class TopMenu extends MenuBar {

	public TopMenu() {

        // Aftertaste menu
        Menu aftertasteMenu = new Menu("Aftertaste");
        MenuItem aftertasteAbout = new AftertasteAbout("About");
        MenuItem aftertasteExit = new AftertasteExit("Exit");
        aftertasteMenu.getItems().addAll(aftertasteAbout, aftertasteExit);

        // Story menu
        Menu storyMenu = new Menu("Story");
        Entry<LoadPath, StoryDescriptor> currentStory = BrewController.getCurrentStory();
        if (currentStory == null) {
		    currentStory = Finder.get().getFirstStory();
            BrewController.setCurrentStory(currentStory);
        }
        RadioMenuItem storyDefault = new RadioMenuItem(currentStory.getValue().getTitle());
        storyDefault.setSelected(true);
        MenuItem gameNew = new GameNew("New game");
        MenuItem gameLoad = new GameLoad("Load saved game...");
        SeparatorMenuItem storySep = new SeparatorMenuItem();
        MenuItem storyLoad = new MenuItem("Add a story...");
        storyMenu.getItems().addAll(storyDefault, gameNew, gameLoad, storySep, storyLoad);

        storyDefault.setOnAction(e -> {
            BrewController.setCurrentStory(Finder.get().getFirstStory());
        });

        // // Game menu
        // Menu gameMenu = new Menu("Game");
        // gameMenu.getItems().addAll(gameNew, gameLoad);

        // add menus
        this.getMenus().add(aftertasteMenu);
        this.getMenus().add(storyMenu);
        // this.getMenus().add(gameMenu);
    }	
}
