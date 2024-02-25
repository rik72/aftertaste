package io.rik72.brew.engine.processing.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.processing.execution.actions.direction.DirectionActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.one.OneActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.two.TwoActionExecutor;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.processing.execution.base.AmbiguousTokenExecutor;
import io.rik72.brew.engine.processing.execution.base.Executor;
import io.rik72.brew.engine.processing.execution.base.UnknownTokensExecutor;
import io.rik72.brew.engine.processing.execution.commands.CommandExecutor;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
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
		japaneseMap.put(new Type[] { Type._d_action, Type.particle, Type.direction }, DirectionActionExecutor.class);
		japaneseMap.put(new Type[] { Type._0_action }, ZeroActionExecutor.class);
		japaneseMap.put(new Type[] { Type._1_action, Type.particle, Type.name }, OneActionExecutor.class);
		japaneseMap.put(new Type[] { Type._2_action, Type.particle, Type.name, Type.particle, Type.name }, TwoActionExecutor.class);

		executorMap = new ParseLocalized<Map<Type[],Class<? extends Executor>>>("ExecutorFactory.map", asciiMap, japaneseMap);
	}

	public static Executor get(WordMap wordMap, boolean toBeConfirmed) throws Exception {
		if (!wordMap.getWords().isEmpty()) {
			List<Type> typeList = new ArrayList<>();
			wordMap.getWords().forEach(w -> typeList.add(w.getType()));
			for (Entry<Type[], Class<? extends Executor>> executorEntry : executorMap.get().entrySet()) {
				Type[] entryTypeList = executorEntry.getKey();
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
				return executorEntry.getValue().getDeclaredConstructor(WordMap.class, boolean.class).newInstance(wordMap, toBeConfirmed);
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
