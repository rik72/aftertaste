package io.rik72.brew.engine.processing.execution.commands;

import java.util.List;

import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.story.Story;
import io.rik72.vati.locale.Translations;

public class CommandInventory extends CommandExecutor {

	protected CommandInventory(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	public Results execute() throws Exception {
		String sep = " ";
		StringBuilder builder = new StringBuilder();

		builder.append(Translations.get("you_currently_own"));
		List<Thing> inventoryList = ThingRepository.get().findByLocation(Story.get().getMainCharacter().getInventory(), true);
		if (inventoryList.size() == 0)
			builder.append("\n   - " + Translations.get("nothing") + " -");
		else {
			for (Thing thing : inventoryList) {
				builder.append(sep).append(thing.getListCanonical());
				sep = ", ";
			}
			builder.append(".");
		}

		return new Results(true, false, builder.toString());
	}
}
