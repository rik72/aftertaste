package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.CharacterStatusPossibility;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.docs.Docs;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.CharacterStatusRaw;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Possibility;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.CharacterRaw;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.PossibilityRaw;
import io.rik72.brew.engine.story.Story;
import io.rik72.mammoth.db.DB;

public class CharacterLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {
		YmlParser ymlParser = new YmlParser(Docs.Characters.class);
		Docs.Characters doc = (Docs.Characters) ymlParser.parse(loadPath, "characters.yml");

		boolean first = true;
		for (CharacterRaw character : doc.characters) {
			Parser.checkNotEmpty("character name", character.name);
			if (first) {
				first = false;
				Location inventory = new Location(Character.inventory(character.name), false);
				DB.persist(inventory);
				Parser.checkNotEmpty("character start location",character.startLocation);
				Character mainCharacter = new Character(character.name, character.startLocation);
				DB.persist(mainCharacter);
				CharacterRepository.get().setMainCharacterId(mainCharacter.getId());
				Parser.checkNotEmpty("character status list", character.statuses);
				boolean firstStatus = true;
				for (CharacterStatusRaw stItem : character.statuses) {
					Parser.checkNotEmpty("character status label", stItem.status);
					CharacterStatus status = new CharacterStatus(character.name, stItem.status);
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
									mainCharacter, pItem.inherit, pItem.verb);
							if (parent == null)
								throw new IllegalParseException("inherit reference to unknown status", pItem.inherit);
							possibility = new CharacterStatusPossibility(
								character.name, stItem.status, parent.getAction().getText(),
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
								character.name, stItem.status, pItem.verb, pItem.possible, pItem.important, pItem.feedback);
						}
						DB.persist(possibility);
					}
				}
				mainCharacter.setStatus("initial");
				DB.persist(mainCharacter);
			}
			else {
				DB.persist(new Character(character.name, character.startLocation));
			}
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	public void reload() {
		YmlParser ymlParser = new YmlParser(Docs.Characters.class);
		Docs.Characters doc = (Docs.Characters) ymlParser.parse("brew/stories/test/characters.yml");

		boolean first = true;
		for (CharacterRaw character : doc.characters) {
			if (first) {
				first = false;
				Character mainCharacter = Story.get().getMainCharacter();
				mainCharacter.setLocation(character.startLocation);
				DB.persist(mainCharacter);
			}
			else {
				DB.persist(new Character(character.name, character.startLocation));
			}
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
