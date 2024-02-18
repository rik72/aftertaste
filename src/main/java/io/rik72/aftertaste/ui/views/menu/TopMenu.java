package io.rik72.aftertaste.ui.views.menu;

import io.rik72.brew.engine.loader.LoadType;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class TopMenu extends MenuBar {

    private Menu storiesMenu;

	public TopMenu() {

        // Aftertaste menu
        Menu aftertasteMenu = new Menu("Aftertaste");
        MenuItem aftertasteAbout = new AftertasteAbout("About");
        MenuItem aftertasteExit = new AftertasteExit("Exit");
        aftertasteMenu.getItems().addAll(aftertasteAbout, aftertasteExit);

        // Story menu
        storiesMenu = new Menu("Stories");

        // add stories
        for (StoryDescriptor descriptor : StoryRegistry.get().getAll())
            addStory(descriptor);
        
        // other items
        MenuItem addStoryFolder = new AddStoryFolder("Add story from folder...");
        MenuItem addStoryCan = new AddStoryCan("Add story from .can file...");
        storiesMenu.getItems().addAll(new SeparatorMenuItem(), addStoryFolder, addStoryCan);

        // add menus
        this.getMenus().add(aftertasteMenu);
        this.getMenus().add(storiesMenu);
    }	

    public void addStory(StoryDescriptor descriptor) {
        Menu thisStoryMenu = new Menu(descriptor.getTitle());
        MenuItem gameNew = new GameNew("New game", descriptor);
        MenuItem gameLoad = new GameLoad("Load saved game...", descriptor);
        thisStoryMenu.getItems().addAll(gameNew, gameLoad);
        if (descriptor.getLoadPath().getLoadType() != LoadType.RESOURCES)
            thisStoryMenu.getItems().addAll(new SeparatorMenuItem(), new RemoveStory("Remove this story...", descriptor));
        storiesMenu.getItems().add(thisStoryMenu);
    }
}
