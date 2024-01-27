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
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.CharacterLoader;
import io.rik72.brew.engine.loader.loaders.LocationLoader;
import io.rik72.brew.engine.loader.loaders.PrepositionLoader;
import io.rik72.brew.engine.loader.loaders.ThingLoader;
import io.rik72.brew.engine.loader.loaders.VerbLoader;
import io.rik72.brew.engine.loader.loaders.WordLoader;
import io.rik72.brew.engine.loader.loaders.docs.Docs;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltas;

public class Story {
	private String artifactId;
	private String version;
	private int saveVersion;
	private String title;
	private String subtitle;
	private List<String> intro = new ArrayList<>();

	private Story() {
	}

	public void init() {
		new PrepositionLoader().register();
		new VerbLoader().register();
		new LocationLoader().register();
		new CharacterLoader().register();
		new ThingLoader().register();
		new WordLoader().register();

		load();
	}

	public Character getMainCharacter() {
		return CharacterRepository.get().getMainCharacter();
	}

	public List<String> getIntro() {
		return intro;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	public StoryRefId getRefId() {
		return new StoryRefId(artifactId, saveVersion);
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	private void load() {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse("brew/stories/test/story.yml");
		
		this.artifactId = doc.story.artifactId;
		this.version = doc.story.version;
		this.saveVersion = doc.story.saveVersion;
		this.title = doc.story.title;
		this.subtitle = doc.story.subtitle;
		for (String item : doc.story.intro)
			this.intro.add(item);
	}

	public void restart() {
		new ThingLoader().reload();
		new LocationLoader().reload();
		new CharacterLoader().reload();
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
