package io.rik72.brew.engine.processing.execution.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.CharacterXTextGroup;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.CharacterXTextGroupRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.utils.TextUtils;

public class CommandMemory extends CommandExecutor {

	protected CommandMemory(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() throws Exception {
		StringBuilder builder = new StringBuilder();

		Word nArg = words.size() == 2 ? words.get(1) : null;

		List<CharacterXTextGroup> memories = CharacterXTextGroupRepository.get().findByCharacter(Story.get().getMainCharacter());
		if (nArg != null) {
			int n = Integer.parseInt(nArg.getText()) - 1;
			if (n > memories.size() - 1)
				return new Results(false, false, String.format("Memory %d not found.", n + 1));
			CharacterXTextGroup m = memories.get(n);
			builder.append("You remember ").append(m.getTextGroup().getName()).append(":");
			List<String> texts = new ArrayList<>(m.getTextGroup().getTextStrings());
			Collections.reverse(texts);
			for (String text : texts) {
				builder.append("\n").append(TextUtils.quote(text));
			}
		}
		else {
			builder.append("You remember:");
			String sep = " ";
			if (memories.size() == 0)
				builder.append("\n   - nothing! -");
			else {
				int i = 1;
				for (CharacterXTextGroup m : memories) {
					builder.append(sep).append(i++).append(". ").append(m.getTextGroup().getName());
					sep = ", ";
				}
				builder.append(".");
			}
		}

		return new Results(true, false, builder.toString());
	}
}
