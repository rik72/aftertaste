package io.rik72.mammoth.db;

import java.util.Stack;

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
	private static Stack<Transaction> txs = new Stack<>();

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

	public static int executeMutationQuery(String queryString)  {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to create query");
		}
		else
			throw new IllegalStateException("No session is present, impossible to create query");

		return session.createMutationQuery(queryString).executeUpdate();
	}

	public static int executeNativeMutationQuery(String queryString)  {
		if (session != null) {
			if (!session.isOpen())
				throw new IllegalStateException("Session is not open, impossible to create query");
		}
		else
			throw new IllegalStateException("No session is present, impossible to create query");

		return session.createNativeMutationQuery(queryString).executeUpdate();
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
		if (session != null) {
			if (session.isOpen()) {
				if (!txs.empty()) {
					Transaction tx = txs.lastElement();
					if (tx.getStatus().isNotOneOf(TransactionStatus.ACTIVE))
						throw new IllegalStateException("Cannot nest a transaction while another one is in state " + tx.getStatus());
				}
				txs.push(session.beginTransaction());
			}
			else
				throw new IllegalStateException("Session is not open, impossible to open transaction");
		}
		else {
			txs.push(getOpenSession().beginTransaction());
		}
	}

	public static void commitTransaction() {
		if (!txs.empty()) {
			Transaction tx = txs.lastElement();
			if (tx.isActive()) {
				tx.commit();
				txs.pop();
			}
			else
				throw new IllegalStateException("Current nested transaction cannot be committed (" + tx.getStatus() + ")");
		}
		// else
		// 	throw new IllegalStateException("No transaction to commit");
	}

	public static void rollbackTransaction() {
		if (!txs.empty()) {
			Transaction tx = txs.lastElement();
			if (tx.isActive()) {
				tx.rollback();
				txs.pop();
			}
			else
				throw new IllegalStateException("Current nested transaction cannot be rolled back (" + tx.getStatus() + ")");
		}
		else
			throw new IllegalStateException("No transaction to roll back");
	}

	///////////////////////////////////////////////////////////////////////////

	private static Session getOpenSession() {
		if (session == null) {
			session = Hibernate.getSessionFactory().openSession();
		}
		return session;
	}

	// private static void closeSession() {
	// 	if (session != null) {
	// 		session.close();
	// 		session = null;
	// 	}
	// }
}
