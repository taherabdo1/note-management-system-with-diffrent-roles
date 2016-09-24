package com.toptal.dao;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import model.Note;
import model.User;

import com.toptal.dao.AbstractDaoImpl;

public class NoteDao extends AbstractDaoImpl<Note> {

	public NoteDao() {
		super(Note.class);
	}

	@Override
	public Note update(Note entity) {
		EntityManager em;
		User user = findByID(entity.getId()).getUser();
		entity.setUser(user);
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.merge(entity);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return entity;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to update Note", ex);
			throw ex;
		}
	}

	public List<Note> getAllOfUser(User user) {
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();

			List<Note> list = (List<Note>) em
					.createQuery(
							" select n from Note n Where n.user.id = :userId")
					.setParameter("userId", user.getId()).getResultList();
			em.getTransaction().commit();
			em.close();
			return list;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAllOfUser ",
					ex);
			throw ex;
		}
	}

}
