package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.aftertaste.ui.skin.Skin;
import io.rik72.aftertaste.ui.skin.SkinData;
import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;

public class StoryLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse(loadPath, "story.yml");
		
		Story.get().clear();
		Story.get().setDescriptor(StoryDescriptor.load(loadPath));
		if (doc.story.intro != null) {
			for (String item : doc.story.intro)
				Story.get().getIntro().add(item.strip());
		}
		
		Skin skinEnum = Skin.getDefaultSkin();
		SkinData skinData = new SkinData(skinEnum.data);
		if (doc.story.skin != null) {

			if (doc.story.skin.name != null) {
				try {
					skinEnum = Skin.valueOf(doc.story.skin.name);
					skinData = new SkinData(skinEnum.data);
				}
				catch (Exception e) {
					throw new IllegalParseException("skin: " + e.getMessage());
				}
			}

			if (doc.story.skin.colorTextFlowNormal != null ||
				doc.story.skin.colorTextFlowHilight != null ||
				doc.story.skin.colorTextFlowEmphasis != null ||
				doc.story.skin.colorTextFlowBg != null ||
				doc.story.skin.colorTextFlowScrollbar != null ||
				doc.story.skin.colorMenuBg != null ||
				doc.story.skin.colorMenuHilight != null ||
				doc.story.skin.colorMenuSeparator != null ||
				doc.story.skin.colorWindowsBg != null ||
				doc.story.skin.colorWindowsText != null ||
				doc.story.skin.colorWindowsLocationImageBg != null ||
				doc.story.skin.colorWindowsButton != null ||
				doc.story.skin.colorWindowsHover != null ||
				doc.story.skin.colorWindowsModalBg != null ||
				doc.story.skin.colorWindowsModalBorder != null) {
					skinEnum = Skin.CUSTOM;
					SkinData storySkinData = new SkinData(
						doc.story.skin.colorTextFlowNormal,
						doc.story.skin.colorTextFlowHilight,
						doc.story.skin.colorTextFlowEmphasis,
						doc.story.skin.colorTextFlowBg,
						doc.story.skin.colorTextFlowScrollbar,
						doc.story.skin.colorMenuBg,
						doc.story.skin.colorMenuHilight,
						doc.story.skin.colorMenuSeparator,
						doc.story.skin.colorWindowsBg,
						doc.story.skin.colorWindowsText,
						doc.story.skin.colorWindowsLocationImageBg,
						doc.story.skin.colorWindowsButton,
						doc.story.skin.colorWindowsHover,
						doc.story.skin.colorWindowsModalBg,
						doc.story.skin.colorWindowsModalBorder);

					skinData.applyOverrides(storySkinData);
			}

			if (skinEnum == Skin.CUSTOM && (skinData == null || !skinData.check())) {
				throw new IllegalParseException("skin data: if CUSTOM skin is specified, all colors must be present");
			}
		}
		Story.get().setSkin(skinEnum, skinData);
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("WORDS ==================================================");
		List<Word> words = WordRepository.get().findAll();
		words.forEach(s -> Log.debug(s));
	}
}
