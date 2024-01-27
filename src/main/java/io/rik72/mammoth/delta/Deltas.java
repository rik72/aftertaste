package io.rik72.mammoth.delta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Deltas implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Class<? extends Delta>, Map<Short, Delta>> deltas;

	private Deltas() {
		this.deltas = new HashMap<>();
	}

	public Map<Class<? extends Delta>, Map<Short, Delta>> getAll() {
		return deltas;
	}

	public void set(Delta delta) {
		if (!deltas.containsKey(delta.getClass()))
			deltas.put(delta.getClass(), new HashMap<>());
		deltas.get(delta.getClass()).put(delta.getId(), delta);
	}

	public void clear() {
		deltas.clear();
	}

	///////////////////////////////////////////////////////////////////////////
	private static Deltas instance = new Deltas();
	public static Deltas get() {
		return instance;
	}

	public static void set(Deltas instance) {
		Deltas.instance = instance;
	}
}
