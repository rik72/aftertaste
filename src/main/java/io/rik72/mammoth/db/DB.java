package io.rik72.mammoth.db;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import io.rik72.mammoth.delta.Deltable;
import io.rik72.mammoth.delta.Deltas;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class DB {
	private static Session session;
	private static Transaction tx;

	public static void persist(Object obj) {
		if (session != null) {
			if (session.isOpen()) {
				session.persist(obj);
				if (obj instanceof Deltable) {
					Deltas.get().set(((Deltable)obj).getDelta());
				}
				return;
			}
			else
				throw new IllegalStateException("Session is not open, impossible to persist");
		}
		throw new IllegalStateException("No session is present");
	}

	public static CriteriaBuilder getCriteriaBuilder() {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to get criteria builder");
		}
		else
			throw new IllegalStateException("No session is present, impossible to get criteria builder");

		return session.getCriteriaBuilder();
	}

	public static <R> Query<R> createQuery(String queryString, Class<R> resultClass)  {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to create query");
		}
		else
			throw new IllegalStateException("No session is present, impossible to create query");

		return session.createQuery(queryString, resultClass);
	}

	public static <R> Query<R> createQuery(CriteriaQuery<R> criteriaQuery)  {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to create query");
		}
		else
			throw new IllegalStateException("No session is present, impossible to create query");

		return session.createQuery(criteriaQuery);
	}

	public static <R> R getEntityById(Class<R> resultClass, short id) {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to get entity by id");
		}
		else
			throw new IllegalStateException("No session is present, impossible to get entity by id");

		return (R) session.get(resultClass, id);
	}

	public static void beginTransaction() {
		if (tx != null) {
			if (tx.isActive())
				return;
			else if (tx.getStatus().isNotOneOf(TransactionStatus.COMMITTED, TransactionStatus.ROLLED_BACK))
				throw new IllegalStateException("A transaction is already present but cannot be used nor discarded (" + tx.getStatus() + ")");
		}
		if (session != null) {
			if (session.isOpen())
				tx = session.beginTransaction();
			else
				throw new IllegalStateException("Session is not open, impossible to open transaction");
		}
		else {
			getOpenSession().beginTransaction();
		}
	}

	public static void commitTransaction() {
		if (tx != null) {
			if (tx.isActive()) {
				tx.commit();
				return;
			}
			else
				throw new IllegalStateException("A transaction is already present but cannot be committed (" + tx.getStatus() + ")");
		}
	}

	public static void rollbackTransaction() {
		if (tx != null) {
			if (tx.isActive()) {
				tx.rollback();
				return;
			}
			else
				throw new IllegalStateException("A transaction is already present but cannot be rolled back (" + tx.getStatus() + ")");
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private static Session getOpenSession() {
		if (session == null) {
			session = Hibernate.getSessionFactory().openSession();
		}
		return session;
	}
}
