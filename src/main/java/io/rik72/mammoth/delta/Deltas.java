package io.rik72.mammoth.delta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	public static void saveToFile(String path) throws Exception {
		FileOutputStream f = new FileOutputStream(new File(path));
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(instance);
	}

	public static void loadFromFile(String path) throws Exception {
		FileInputStream f = new FileInputStream(new File(path));
		ObjectInputStream i = new ObjectInputStream(f);
		instance = (Deltas)i.readObject();
	}

	///////////////////////////////////////////////////////////////////////////
	private static Deltas instance = new Deltas();
	public static Deltas get() {
		return instance;
	}
}
