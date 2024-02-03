package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.CharacterStatusPossibility;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Helpers;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Possibility;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterStatusRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.PossibilityRaw;
import io.rik72.mammoth.db.DB;

public class CharacterLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {
		YmlParser ymlParser = new YmlParser(Docs.Characters.class);
		Docs.Characters doc = (Docs.Characters) ymlParser.parse(loadPath, "characters.yml");

		// insert words for characters
		for (CharacterRaw chItem : doc.characters) {
			Helpers.loadWord(chItem.word, Type.name, EntityType.character);
			Parser.checkNotEmpty("character name", chItem.name);
			for (CharacterStatusRaw stItem : chItem.statuses)
				Helpers.loadWord(stItem.word, Type.name, EntityType.character);
		}

		// 2nd pass for data and statuses
		for (CharacterRaw chItem : doc.characters) {
			Parser.checkNotEmpty("character name", chItem.name);
			Location inventory = new Location(Character.inventory(chItem.name));
			DB.persist(inventory);
			Parser.checkNotEmpty("character start location",chItem.startLocation);
			Character character = new Character(chItem.name, chItem.startLocation);
			if (!chItem.visible)
				character.setVisible(false);
			DB.persist(character);
			if (chItem.main)
				CharacterRepository.get().setMainCharacterId(character.getId());
			Parser.checkNotEmpty("character status list", chItem.statuses);
			boolean firstStatus = true;
			for (CharacterStatusRaw stItem : chItem.statuses) {
				Parser.checkNotEmpty("character status label", stItem.status);
				CharacterStatus status = new CharacterStatus(
					chItem.name, stItem.status, stItem.brief, stItem.description,
					stItem.word != null ? stItem.word.text : chItem.word.text,
					stItem.finale != null ? stItem.finale.strip() : null);
				DB.persist(status);
				if (firstStatus && !"initial".equals(stItem.status))
					throw new IllegalParseException("character status list: first item must be the 'initial' status");
				firstStatus = false;
				if (stItem.possibilities != null) for (PossibilityRaw pDoc : stItem.possibilities) {
					Possibility pItem = Possibility.parse(pDoc);
					CharacterStatusPossibility possibility;
					if (pItem.inherit != null) {
						CharacterStatusPossibility parent =
							CharacterStatusPossibilityRepository.get().getByCharacterStatusAction(
								character, pItem.inherit, pItem.verb);
						if (parent == null)
							throw new IllegalParseException("inherit reference to unknown status", pItem.inherit);
						possibility = new CharacterStatusPossibility(
							chItem.name, stItem.status, parent.getAction().getText(),
							parent.isPossible(), parent.isImportant(), parent.getFeedback());
						if (pItem.possible != null)
							possibility.setPossible(pItem.possible);
						if (pItem.important != null)
							possibility.setImportant(pItem.important);
						if (pItem.feedback != null)
							possibility.setFeedback(pItem.feedback);
					}
					else {
						possibility = new CharacterStatusPossibility(
							chItem.name, stItem.status, pItem.verb, pItem.possible, pItem.important, pItem.feedback);
					}
					DB.persist(possibility);
				}
			}
			character.setStatus("initial");
			DB.persist(character);
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	public void reset() {
		YmlParser ymlParser = new YmlParser(Docs.Characters.class);
		Docs.Characters doc = (Docs.Characters) ymlParser.parse("brew/stories/test/characters.yml");

		for (CharacterRaw chItem : doc.characters) {
			Character character = CharacterRepository.get().getByName(chItem.name);
			character.setLocation(chItem.startLocation);
			character.setStatus("initial");
			DB.persist(character);
		}
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("CHARACTERS =============================================");
		List<Character> characters = CharacterRepository.get().findAll();
		characters.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER STATUSES =====================================");
		List<CharacterStatus> statuses = CharacterStatusRepository.get().findAll();
		statuses.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER STATUS POSSIBILITIES =========================");
		List<CharacterStatusPossibility> possibilities = CharacterStatusPossibilityRepository.get().findAll();
		possibilities.forEach(s -> Log.debug(s));
	}
}
