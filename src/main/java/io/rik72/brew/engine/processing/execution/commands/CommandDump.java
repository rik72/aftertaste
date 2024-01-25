package io.rik72.brew.engine.processing.execution.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterOneActionRepository;
import io.rik72.brew.engine.db.repositories.CharacterTwoActionRepository;
import io.rik72.brew.engine.db.repositories.LocationOneActionRepository;
import io.rik72.brew.engine.db.repositories.LocationTwoActionRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingOneActionRepository;
import io.rik72.brew.engine.db.repositories.ThingTwoActionRepository;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.repositories.AbstractRepository;

public class CommandDump extends CommandExecutor {

	public CommandDump(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	private static Map<String, AbstractRepository<?>> repoMap = new HashMap<>();

	static {
        repoMap.put("character", 					CharacterRepository.get());
        repoMap.put("characterstatus", 				CharacterStatusRepository.get());
        repoMap.put("characterstatuspossibility", 	CharacterStatusPossibilityRepository.get());
        repoMap.put("characteroneaction", 			CharacterOneActionRepository.get());
        repoMap.put("charactertwoaction", 			CharacterTwoActionRepository.get());
        repoMap.put("location", 					LocationRepository.get());
        repoMap.put("locationstatus", 				LocationStatusRepository.get());
        repoMap.put("locationxlocation", 			LocationXLocationRepository.get());
        repoMap.put("locationstatuspossibility", 	LocationStatusPossibilityRepository.get());
        repoMap.put("locationoneaction", 			LocationOneActionRepository.get());
        repoMap.put("locationtwoaction", 			LocationTwoActionRepository.get());
        repoMap.put("thing", 						ThingRepository.get());
        repoMap.put("thingstatus", 					ThingStatusRepository.get());
        repoMap.put("thingstatuspossibility", 		ThingStatusPossibilityRepository.get());
        repoMap.put("thingoneaction", 				ThingOneActionRepository.get());
        repoMap.put("thingtwoaction", 				ThingTwoActionRepository.get());
        repoMap.put("word", 						WordRepository.get());
	}
	@Override
	public Results execute() {

		Word entity = words.size() >= 2 ? words.get(1) : null;
		Word id = words.size() >= 3 ? words.get(2) : null;

		if (entity == null)
			return new Results(false, false, "Usage: dump <entity> [ <id> ]");
		AbstractRepository<?> repo = repoMap.get(entity.getText());
		if (repo == null)
			return new Results(false, false, "Usage: dump <entity> [ <id> ]");

		List<Object> dumps = new ArrayList<>();
		if (id == null)
			dumps.addAll(repo.findAll());
		else
			dumps.add(repo.getById(Short.parseShort(id.getText())));

		Log.skip();
		dumps.forEach(s -> Log.debug(s));

		return new Results(true);
	}
}
