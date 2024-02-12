package io.rik72.aftertaste.ui.views.menu;

import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class TopMenu extends MenuBar {

    private Menu storyMenu;

	public TopMenu() {

        // Aftertaste menu
        Menu aftertasteMenu = new Menu("Aftertaste");
        MenuItem aftertasteAbout = new AftertasteAbout("About");
        MenuItem aftertasteExit = new AftertasteExit("Exit");
        aftertasteMenu.getItems().addAll(aftertasteAbout, aftertasteExit);

        // Story menu
        storyMenu = new Menu("Story");
        SeparatorMenuItem storySep = new SeparatorMenuItem();
        MenuItem storyLoad = new AddStory("Add a story...");
        storyMenu.getItems().addAll(storySep, storyLoad);

        // add stories
        int storyPosition = 1;
        addStory(storyPosition++, StoryRegistry.get().getEmbeddedStory());
        for (StoryDescriptor descriptor : StoryRegistry.get().getUserStories()) {
            addStory(storyPosition++, descriptor);
        }
        // storyDefault.setOnAction(e -> {
        //     BrewController.setCurrentStory(StoryRegistry.get().getEmbeddedStory());
        // });

        // add menus
        this.getMenus().add(aftertasteMenu);
        this.getMenus().add(storyMenu);
    }	

    public void addStory(int position, StoryDescriptor descriptor) {
        MenuItem gameNew = new GameNew("New game", descriptor);
        MenuItem gameLoad = new GameLoad("Load saved game...", descriptor);
        Menu thisStoryMenu = new Menu(descriptor.getTitle());
        thisStoryMenu.getItems().addAll(gameNew, gameLoad);
        storyMenu.getItems().add(position, thisStoryMenu);
    }
}
