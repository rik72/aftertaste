package io.rik72.brew.engine.cleaner;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.*;
import io.rik72.mammoth.db.DB;

public class Cleaner {

	private Cleaner() {}

	public void run() throws Exception {
        try {
            DB.beginTransaction();

			CharacterStatusPossibilityRepository.get().deleteAll();
			CharacterOnCharacterRepository.get().deleteAll();
			CharacterThingOnCharacterRepository.get().deleteAll();
			CharacterOnLocationRepository.get().deleteAll();
			CharacterThingOnLocationRepository.get().deleteAll();
			CharacterOnThingRepository.get().deleteAll();
			CharacterThingOnThingRepository.get().deleteAll();
			ThingCharacterOnCharacterRepository.get().deleteAll();
			ThingOnCharacterRepository.get().deleteAll();
			ThingThingOnCharacterRepository.get().deleteAll();
			ThingCharacterOnLocationRepository.get().deleteAll();
			ThingCharacterOnThingRepository.get().deleteAll();
			CharacterStatusRepository.get().deleteAll();
			CharacterRepository.get().deleteAll();

			ThingOnLocationRepository.get().deleteAll();
			ThingThingOnLocationRepository.get().deleteAll();
			ThingStatusPossibilityRepository.get().deleteAll();
			ThingOnThingRepository.get().deleteAll();
			ThingThingOnThingRepository.get().deleteAll();
			ThingStatusRepository.get().deleteAll();
			ThingRepository.get().deleteAll();

			LocationStatusPossibilityRepository.get().deleteAll();
			LocationXLocationRepository.get().deleteAll();
			LocationStatusRepository.get().deleteAll();
			LocationRepository.get().deleteAll();
			
			WordRepository.get().deleteAll();

            DB.commitTransaction();
        } catch (Exception e) {
            DB.rollbackTransaction();
            throw e;
        }

		Log.skip();
		Log.debug("WORDS ==================================================");
		List<Word> words = WordRepository.get().findAll();
		words.forEach(s -> Log.debug(s));
		Log.skip();
	}

	///////////////////////////////////////////////////////////////////////////
	private static Cleaner instance = new Cleaner();
	public static Cleaner get() {
		return instance;
	}
}
