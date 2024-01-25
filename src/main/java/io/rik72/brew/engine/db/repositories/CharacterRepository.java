package io.rik72.brew.engine.db.repositories;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.mammoth.repositories.AbstractRepository;

public class CharacterRepository extends AbstractRepository<Character> {

	private short mainCharacterId;

	private CharacterRepository() {
		super(Character.class);
	}

	public void setMainCharacterId(short mainCharacterId) {
		this.mainCharacterId = mainCharacterId;
	}

	public Character getMainCharacter() {
		return getById(mainCharacterId);
	}

	public Character getByName(String value) {
		return getByField("name", value);
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterRepository instance = new CharacterRepository();
	public static CharacterRepository get() {
		return instance;
	}
}
