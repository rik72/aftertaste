package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.aftertaste.ui.skin.AftertasteFont;
import io.rik72.aftertaste.ui.skin.Skin;
import io.rik72.aftertaste.ui.skin.SkinData;
import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.vati.core.VatiLocale;

public class StoryLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse(loadPath, "story.yml");
		
		Story.get().clear();
		Story.get().setDescriptor(StoryDescriptor.load(loadPath));
		if (doc.story.intro != null) {
			TextGroup group = TextGroupRepository.get().getByName(doc.story.intro);
			Story.get().getIntro().addAll(group.getTextStrings());
			Story.get().setIntroId(group.getId());
		}

		VatiLocale localeEnum = VatiLocale.getDefault();
		if (doc.story.locale != null) {
			try {
				localeEnum = VatiLocale.valueOf(doc.story.locale.strip().toUpperCase());
			}
			catch (Exception e) {
				throw new IllegalParseException("locale: " + e.getMessage());
			}
		}
		Story.get().setLocale(localeEnum);
		VatiLocale.setCurrent(localeEnum);

		Skin skinEnum = Skin.getDefault();
		SkinData skinData = new SkinData(skinEnum.data);
		if (doc.story.skin != null) {

			if (doc.story.skin.name != null) {
				try {
					skinEnum = Skin.valueOf(doc.story.skin.name.strip().toUpperCase());
					skinData = new SkinData(skinEnum.data);
				}
				catch (Exception e) {
					throw new IllegalParseException("skin: " + e.getMessage());
				}
			}

			if (doc.story.skin.fontFamily != null ||
				doc.story.skin.fontSize != null ||
			    doc.story.skin.colorTextFlowNormal != null ||
				doc.story.skin.colorTextFlowHilight != null ||
				doc.story.skin.colorTextFlowEmphasis != null ||
				doc.story.skin.colorTextFlowBg != null ||
				doc.story.skin.colorTextFlowScrollbar != null ||
				doc.story.skin.colorMenuBg != null ||
				doc.story.skin.colorMenuHilight != null ||
				doc.story.skin.colorMenuSeparator != null ||
				doc.story.skin.colorWindowBg != null ||
				doc.story.skin.colorWindowText != null ||
				doc.story.skin.colorWindowLocationImageBg != null ||
				doc.story.skin.colorWindowButton != null ||
				doc.story.skin.colorWindowHover != null ||
				doc.story.skin.colorWindowModalBg != null ||
				doc.story.skin.colorWindowModalBorder != null) {
					skinEnum = Skin.CUSTOM;
					SkinData storySkinData = new SkinData(
						AftertasteFont.valueOf(doc.story.skin.fontFamily.strip().toUpperCase()),
						doc.story.skin.fontSize,
						doc.story.skin.colorTextFlowNormal,
						doc.story.skin.colorTextFlowHilight,
						doc.story.skin.colorTextFlowEmphasis,
						doc.story.skin.colorTextFlowBg,
						doc.story.skin.colorTextFlowScrollbar,
						doc.story.skin.colorMenuBg,
						doc.story.skin.colorMenuHilight,
						doc.story.skin.colorMenuSeparator,
						doc.story.skin.colorWindowBg,
						doc.story.skin.colorWindowText,
						doc.story.skin.colorWindowLocationImageBg,
						doc.story.skin.colorWindowButton,
						doc.story.skin.colorWindowHover,
						doc.story.skin.colorWindowModalBg,
						doc.story.skin.colorWindowModalBorder);

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
