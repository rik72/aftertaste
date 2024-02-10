package io.rik72.mammoth.repositories;

import java.util.List;

import io.rik72.mammoth.MultipleResultsException;
import io.rik72.mammoth.db.DB;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class AbstractRepository <T> {

	protected Class<T> entityClass;

	protected AbstractRepository(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public List<T> findAll() {
		return DB.createQuery(
			"FROM " + entityClass.getSimpleName(),
			entityClass).list();
	}

	public T getById(short id) {
		return DB.getEntityById(entityClass, id);
	}

	public T getByField(String field, Object value) {
		List<T> results = findByField(field, value);
		if (results.size() > 1)
			throw new MultipleResultsException(
				"Multiple results found (" + entityClass.getSimpleName() + "::" + field + "=" + value + ")",
				value.toString());
		return results.size() == 1 ? results.get(0) : null;
	}

	public List<T> findByField(String field, Object value) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(entityClass);
		Root<T> root = cr.from(entityClass);
		cr.select(root).where(cb.equal(root.get(field), value));
		return DB.createQuery(cr).list();
	}

	public void deleteAll() {
		DB.executeMutationQuery("DELETE " + entityClass.getSimpleName());
		// DB.executeNativeMutationQuery("ALTER TABLE " + entityClass.getSimpleName() + " ALTER COLUMN id RESTART WITH 1");
	}

}
