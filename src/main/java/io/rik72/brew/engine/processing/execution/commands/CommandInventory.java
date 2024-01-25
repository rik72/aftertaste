package io.rik72.brew.engine.processing.execution.commands;

import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.Story;

public class CommandInventory extends CommandExecutor {

	protected CommandInventory(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() throws Exception {
		String sep = " ";
		StringBuilder builder = new StringBuilder();

		builder.append("You currently own:");
		List<Thing> inventoryList = ThingRepository.get().findByLocation(Story.get().getMainCharacter().getInventory(), true);
		if (inventoryList.size() == 0)
			builder.append("\n   - nothing! -");
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
