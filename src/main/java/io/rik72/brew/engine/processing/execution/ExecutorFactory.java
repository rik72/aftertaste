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

public class ExecutorFactory {

	private static Map<Type[], Class<? extends Executor>> executorMap = new HashMap<>();

	static {
		executorMap.put(new Type[] { Type.COMMAND }, CommandExecutor.class);
		executorMap.put(new Type[] { Type.COMMAND, Type.NAME }, CommandExecutor.class);
		executorMap.put(new Type[] { Type.COMMAND, Type.NAME, Type.NUMBER }, CommandExecutor.class);
		executorMap.put(new Type[] { Type.DIRECTION }, ZeroActionExecutor.class);
		executorMap.put(new Type[] { Type._D_ACTION,  Type.DIRECTION }, DirectionActionExecutor.class);
		executorMap.put(new Type[] { Type._0_ACTION }, ZeroActionExecutor.class);
		executorMap.put(new Type[] { Type._1_ACTION, Type.NAME }, OneActionExecutor.class);
		executorMap.put(new Type[] { Type._2_ACTION, Type.NAME, Type.PREPOSITION, Type.NAME }, TwoActionExecutor.class);
	}

	public static Executor get(Vector<Word> words, boolean toBeConfirmed) throws Exception {
		if (!words.isEmpty()) {
			List<Type> typeList = new ArrayList<>();
			words.forEach(w -> typeList.add(w.getType()));
			for (Entry<Type[], Class<? extends Executor>> entry : executorMap.entrySet()) {
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

	public static Executor getAmbiguousExecutor(Vector<Word> words, String offendingToken) {
		return new AmbiguousExecutor(words, offendingToken);
	}
}
