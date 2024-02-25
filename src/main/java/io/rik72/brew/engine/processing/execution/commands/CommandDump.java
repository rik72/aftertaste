package io.rik72.brew.engine.processing.execution.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingOnThingRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnThingRepository;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.db.repositories.abstractions.ComplementRepository;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.mammoth.repositories.AbstractRepository;

public class CommandDump extends CommandExecutor {

	public CommandDump(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	private static Map<String, AbstractRepository<?>> repoMap = new HashMap<>();

	static {
        repoMap.put("character", 					CharacterRepository.get());
        repoMap.put("characterstatus", 				CharacterStatusRepository.get());
        repoMap.put("characterstatuspossibility", 	CharacterStatusPossibilityRepository.get());
        repoMap.put("thingoncharacter", 			ThingOnCharacterRepository.get());
        repoMap.put("thingthingoncharacter", 		ThingThingOnCharacterRepository.get());
        repoMap.put("location", 					LocationRepository.get());
        repoMap.put("locationstatus", 				LocationStatusRepository.get());
        repoMap.put("locationxlocation", 			LocationXLocationRepository.get());
        repoMap.put("locationstatuspossibility", 	LocationStatusPossibilityRepository.get());
        repoMap.put("thingonlocation", 				ThingOnLocationRepository.get());
        repoMap.put("thingthingonlocation", 		ThingThingOnLocationRepository.get());
        repoMap.put("thing", 						ThingRepository.get());
        repoMap.put("thingstatus", 					ThingStatusRepository.get());
        repoMap.put("thingstatuspossibility", 		ThingStatusPossibilityRepository.get());
        repoMap.put("thingonthing", 				ThingOnThingRepository.get());
        repoMap.put("thingthingonthing", 			ThingThingOnThingRepository.get());
        repoMap.put("word", 						WordRepository.get());
	}
	@Override
	public Results execute() {

		Vector<Word> words = wordMap.getWords();
		Word entity = words.size() >= 2 ? words.get(1) : null;
		Word id = words.size() >= 3 ? words.get(2) : null;

		if (entity == null)
			return new Results(false, false, "Usage: dump <entity> [ <id> | \"*\"<name - only for character, location, thing> ]");

		List<Object> dumps = new ArrayList<>();
		AbstractRepository<?> repo = repoMap.get(entity.getText());
		if (repo == null) {
			if (entity.getType() == Type.entity) {
				repo = repoMap.get(entity.getEntityType().toString());
				dumps.add(((ComplementRepository<?>)repo).getByName(entity.getText()));
			}
			else {
				return new Results(false, false, "Usage: dump <entity> [ <id> | \"*\"<name - only for character, location, thing> ]");
			}
		}
		else if (id == null)
			dumps.addAll(repo.findAll());
		else if (id.getType() == Type.number)
			dumps.add(repo.getById(Short.parseShort(id.getText())));

		Log.skip();
		dumps.forEach(s -> Log.debug(s));

		return new Results(true);
	}
}
