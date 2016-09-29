package com.toptal.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
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
							" select n from Note n Where n.user.email = :userEmail")
					.setParameter("userEmail", user.getEmail()).getResultList();
			em.getTransaction().commit();
			em.close();
			return list;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAllOfUser ", ex);
			throw ex;
		}
	}

	// this will return a list of object, each entry in the list will represent
	// all notes in a date and their total period
	public List<Object[]> getFilteredNotesByDateForUser(Date startDate,
			Date endDate, User user) {

		System.out.println(startDate + " start date *********");
		System.out.println(endDate+ " end date *********");
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = sdf.format(startDate);

			String date2 = sdf.format(endDate);

			List<Object[]> list = (List<Object[]>) em
					.createNativeQuery(
							" SELECT start_date, sum(period),  GROUP_CONCAT(description) FROM Note n , User u where u.email = :email group by start_date having start_date between :date1 and :date2")
					.setParameter("date1", date1).setParameter("date2", date2)
					.setParameter("email", user.getEmail()).getResultList();
			em.getTransaction().commit();
			em.close();

			return list;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAllOfUser ", ex);
			throw ex;
		}

	}

}
