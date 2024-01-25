package io.rik72.brew.engine.loader.loaders.docs.types.raw;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public abstract class Raw {
	
	@Override
	public String toString() {
		List<Field> allFields = Arrays.asList(this.getClass().getDeclaredFields());
		StringBuilder buf = new StringBuilder();
		buf.append("{ ");
		for (Field field : allFields) {
			try {
				if (!Modifier.isStatic(field.getModifiers())) {
					buf.append(field.getName()).append("=");
					if (field.getType() == String.class)
						buf.append("\"");
					buf.append(field.get(this));
					if (field.getType() == String.class)
						buf.append("\"");
					buf.append(" ");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		buf.append("}");
		return buf.toString();
	}
}
