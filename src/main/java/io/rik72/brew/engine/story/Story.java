package io.rik72.brew.engine.story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.rik72.brew.engine.db.delta.CharacterDelta;
import io.rik72.brew.engine.db.delta.LocationDelta;
import io.rik72.brew.engine.db.delta.ThingDelta;
import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.loader.loaders.CharacterLoader;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.loaders.ActionLoader;
import io.rik72.brew.engine.loader.loaders.LocationLoader;
import io.rik72.brew.engine.loader.loaders.PrepositionLoader;
import io.rik72.brew.engine.loader.loaders.StoryLoader;
import io.rik72.brew.engine.loader.loaders.ThingLoader;
import io.rik72.brew.engine.loader.loaders.VerbLoader;
import io.rik72.brew.engine.loader.loaders.WordLoader;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltas;

public class Story {
	private StoryDescriptor descriptor;
	private List<String> intro = new ArrayList<>();

	private Story() {
	}

	public void clear() {
		this.descriptor = null;
		intro.clear();
	}

	public void init() {
		new StoryLoader().register();
		new PrepositionLoader().register();
		new VerbLoader().register();
		new WordLoader().register();
		new LocationLoader().register();
		new CharacterLoader().register();
		new ThingLoader().register();
		new ActionLoader().register();
	}

	public Character getMainCharacter() {
		return CharacterRepository.get().getMainCharacter();
	}

	public List<String> getIntro() {
		return intro;
	}

	public StoryDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(StoryDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public StoryRefId getRefId() {
		return descriptor.getRefId();
	}

	public String getTitle() {
		return descriptor.getTitle();
	}

	public String getSubtitle() {
		return descriptor.getSubtitle();
	}

	public void restart() throws Exception {
		LoadPath loadPath = descriptor.getLoadPath();
		new ThingLoader().reset(loadPath);
		new LocationLoader().reset(loadPath);
		new CharacterLoader().reset(loadPath);
	}

	public void applyDeltas() {
		Map<Class<? extends Delta>, Map<Short, Delta>> deltas = Deltas.get().getAll();
		for (Entry<Class<? extends Delta>, Map<Short, Delta>> mapEntry : deltas.entrySet()) {
			if (mapEntry.getKey() == ThingDelta.class) {
				for (Entry<Short, Delta> deltaEntry : mapEntry.getValue().entrySet()) {
					Short id = deltaEntry.getKey();
					ThingDelta delta = (ThingDelta)deltaEntry.getValue();
					Thing thing = ThingRepository.get().getById(id);
					thing.setVisible(delta.isVisible());
					thing.setTakeable(delta.isTakeable());
					thing.setDroppable(delta.isDroppable());
					thing.setLocation(LocationRepository.get().getById(delta.getLocationId()));
					thing.setStatus(ThingStatusRepository.get().getById(delta.getStatusId()));
					DB.persist(thing);
				}
			}
			else if (mapEntry.getKey() == LocationDelta.class) {
				for (Entry<Short, Delta> deltaEntry : mapEntry.getValue().entrySet()) {
					Short id = deltaEntry.getKey();
					LocationDelta delta = (LocationDelta)deltaEntry.getValue();
					Location location = LocationRepository.get().getById(id);
					location.setStatus(LocationStatusRepository.get().getById(delta.getStatusId()));
					DB.persist(location);
				}
			}
			else if (mapEntry.getKey() == CharacterDelta.class) {
				for (Entry<Short, Delta> deltaEntry : mapEntry.getValue().entrySet()) {
					Short id = deltaEntry.getKey();
					CharacterDelta delta = (CharacterDelta)deltaEntry.getValue();
					Character character = CharacterRepository.get().getById(id);
					character.setLocation(LocationRepository.get().getById(delta.getLocationId()));
					DB.persist(character);
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	private static Story instance = new Story();
	public static Story get() {
		return instance;
	}
}
