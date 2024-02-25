package io.rik72.brew.engine.processing.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.processing.execution.actions.direction.DirectionActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.one.OneActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.two.TwoActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.processing.execution.commands.CommandExecutor;
import io.rik72.vati.parselocale.ParseLocalized;

public class ExecutorFactory {

	private static ParseLocalized<Map<Type[], Class<? extends Executor>>> executorMap;

	static {
		Map<Type[], Class<? extends Executor>> asciiMap = new HashMap<>();
		asciiMap.put(new Type[] { Type.command }, CommandExecutor.class);
		asciiMap.put(new Type[] { Type.command, Type.number }, CommandExecutor.class);
		asciiMap.put(new Type[] { Type.command, Type.name }, CommandExecutor.class);
		asciiMap.put(new Type[] { Type.command, Type.name, Type.number }, CommandExecutor.class);
		asciiMap.put(new Type[] { Type.command, Type.entity }, CommandExecutor.class);
		asciiMap.put(new Type[] { Type.direction }, ZeroActionExecutor.class);
		asciiMap.put(new Type[] { Type._d_action,  Type.direction }, DirectionActionExecutor.class);
		asciiMap.put(new Type[] { Type._0_action }, ZeroActionExecutor.class);
		asciiMap.put(new Type[] { Type._1_action, Type.name }, OneActionExecutor.class);
		asciiMap.put(new Type[] { Type._2_action, Type.name, Type.preposition, Type.name }, TwoActionExecutor.class);

		Map<Type[], Class<? extends Executor>> japaneseMap = new HashMap<>();
		japaneseMap.put(new Type[] { Type.command }, CommandExecutor.class);
		japaneseMap.put(new Type[] { Type.command, Type.number }, CommandExecutor.class);
		japaneseMap.put(new Type[] { Type.command, Type.name }, CommandExecutor.class);
		japaneseMap.put(new Type[] { Type.command, Type.name, Type.number }, CommandExecutor.class);
		japaneseMap.put(new Type[] { Type.command, Type.entity }, CommandExecutor.class);
		japaneseMap.put(new Type[] { Type.direction }, ZeroActionExecutor.class);
		japaneseMap.put(new Type[] { Type.direction, Type.particle, Type._d_action }, DirectionActionExecutor.class);
		japaneseMap.put(new Type[] { Type._0_action }, ZeroActionExecutor.class);
		japaneseMap.put(new Type[] { Type.name, Type.particle, Type._1_action}, OneActionExecutor.class);
		japaneseMap.put(new Type[] { Type.name, Type.particle, Type.name, Type.particle, Type._2_action}, TwoActionExecutor.class);

		executorMap = new ParseLocalized<Map<Type[],Class<? extends Executor>>>("ExecutorFactory.map", asciiMap, japaneseMap);
	}

	public static Executor get(Vector<Word> words, boolean toBeConfirmed) throws Exception {
		if (!words.isEmpty()) {
			List<Type> typeList = new ArrayList<>();
			words.forEach(w -> typeList.add(w.getType()));
			for (Entry<Type[], Class<? extends Executor>> entry : executorMap.get().entrySet()) {
				Type[] entryTypeList = entry.getKey();
				if (typeList.size() != entryTypeList.length)
					continue;
				boolean found = true;
				for (int i=0; i < typeList.size(); i++) {
					if (!typeList.get(i).equals(entryTypeList[i])) {
						found = false;
						break;
					}
				}
				if (!found)
					continue;
				return entry.getValue().getDeclaredConstructor(Vector.class, boolean.class).newInstance(words, toBeConfirmed);
			}
		}
		return null;
	}

	public static Executor getAmbiguousTokenExecutor(String offendingToken) {
		return new AmbiguousTokenExecutor(offendingToken);
	}

	public static Executor getUnknownTokensExecutor(List<String> offendingTokens) {
		return new UnknownTokensExecutor(offendingTokens);
	}
}
