package io.rik72.aftertaste.ui.views.menu;

import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import io.rik72.brew.game.BrewController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class TopMenu extends MenuBar {

    private Menu storyMenu;
    private ToggleGroup storyToggleGroup = new ToggleGroup();

	public TopMenu() {

        // Aftertaste menu
        Menu aftertasteMenu = new Menu("Aftertaste");
        MenuItem aftertasteAbout = new AftertasteAbout("About");
        MenuItem aftertasteExit = new AftertasteExit("Exit");
        aftertasteMenu.getItems().addAll(aftertasteAbout, aftertasteExit);

        // Story menu
        storyMenu = new Menu("Story");
        StoryDescriptor currentStory = BrewController.getCurrentStory();
        if (currentStory == null) {
		    currentStory = StoryRegistry.get().getEmbeddedStory();
            BrewController.setCurrentStory(currentStory);
        }
        RadioMenuItem storyDefault = new RadioMenuItem(currentStory.getTitle());
        storyDefault.setToggleGroup(storyToggleGroup);
        storyDefault.setSelected(true);
        MenuItem gameNew = new GameNew("New game");
        MenuItem gameLoad = new GameLoad("Load saved game...");
        SeparatorMenuItem storySep1 = new SeparatorMenuItem();
        SeparatorMenuItem storySep2 = new SeparatorMenuItem();
        MenuItem storyLoad = new AddStory("Add a story...");
        storyMenu.getItems().addAll(storyDefault, storySep1, gameNew, gameLoad, storySep2, storyLoad);

        for (StoryDescriptor descriptor : StoryRegistry.get().getUserStories()) {
            addUserStory(descriptor);
        }

        storyDefault.setOnAction(e -> {
            BrewController.setCurrentStory(StoryRegistry.get().getEmbeddedStory());
        });

        // add menus
        this.getMenus().add(aftertasteMenu);
        this.getMenus().add(storyMenu);
    }	

    public void addUserStory(StoryDescriptor descriptor) {
        RadioMenuItem userStoryRadio = new RadioMenuItem(descriptor.getTitle());
        userStoryRadio.setToggleGroup(storyToggleGroup);
        storyMenu.getItems().add(1, userStoryRadio);
        userStoryRadio.setSelected(true);
    }
}
