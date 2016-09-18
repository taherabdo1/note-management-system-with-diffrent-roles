package com.toptal.dao;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {

	private Class<T> type;

	private EntityManager em;
	protected Logger log = Logger.getLogger(AbstractDaoImpl.class.getName());

	public AbstractDaoImpl(Class<T> type) {
		this.type = type;
	}

	public EntityManager getEntityManager() {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("timeManagementPU");
			em = emf.createEntityManager();
		return em;
	}

	@Override
	public T findByID(Integer id) {
		T result;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			result = (T) em.find(type, id);
			em.getTransaction().commit();
			em.close();
			return result;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to update " + type.getSimpleName(),
					ex);
			throw ex;
		}
	}

	@Override
	public T persist(T entity) {
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return entity;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to update " + type.getSimpleName(),
					ex);
			throw ex;
		}
	}

	@Override
	public T update(T entity) {
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.merge(entity);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return entity;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to update " + type.getSimpleName(),
					ex);
			throw ex;
		}
	}

	@Override
	public void delete(T entity) {
		try {
			em = getEntityManager();
			T rem = em.merge(entity);
			em.remove(rem);
			em.flush();
		} catch (PersistenceException ex) {
			log.log(Level.SEVERE, "unable to persist " + type.getSimpleName(),
					ex);
			throw ex;
		}
	}

	@Override
	public Set<T> getAll() {
		try {
			em = getEntityManager();
			em.getTransaction().begin();

			Set<T> list = (Set<T>) em.createQuery(
					" select t from " + type.getSimpleName() + " t")
					.getResultList();
			em.getTransaction().commit();
			em.close();
			return list;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to update " + type.getSimpleName(),
					ex);
			throw ex;
		}
	}

	public void refresh(T obj){
		em.refresh(obj);
	}
}
