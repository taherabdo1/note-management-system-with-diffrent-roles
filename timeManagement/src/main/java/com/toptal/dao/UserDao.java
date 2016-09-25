package com.toptal.dao;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import model.*;

import com.toptal.dao.AbstractDaoImpl;

public class UserDao extends AbstractDaoImpl<User> {

	public UserDao() {
		super(User.class);
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			List<User> users = em.createQuery(" select t from User t")
					.getResultList();
			System.out.println(users.get(0).getEmail());
			// get the role of the lazy mode
			for (User user : users) {
				user.getNotes();
			}
			em.getTransaction().commit();
			em.close();

			return users;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}

	public User signIn(String email, String password) throws Exception{
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			User user = (User) em
					.createQuery(
							" select u from User u Where u.email=:email and u.password = :password")
					.setParameter("email", email)
					.setParameter("password", password).getSingleResult();
			// get the role of the lazy mode
//			if (user != null)
//				user.getNotes();
			em.getTransaction().commit();
			em.close();
			return user;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}

	@Override
	public User update(User user) {
		EntityManager em;
		try {
			User oldUser = findByEmail(user.getEmail());
			user.setId(oldUser.getId());
			// check if the new data is not complete get it from the persisted
			// user data
			if (null == user.getFirstName() || user.getFirstName().equals("")) {
				user.setFirstName(oldUser.getFirstName());
			}
			if (null == user.getLastName() || user.getLastName().equals("")) {
				user.setLastName(oldUser.getLastName());
			}
			if (null == user.getPassword() || user.getPassword().equals("")) {
				user.setPassword(oldUser.getPassword());
			}
			if (null == user.getRole() || user.getRole().equals("")) {
				user.setRole(oldUser.getRole());
			}
			em = getEntityManager();
			em.getTransaction().begin();
			User updatedUser = (User) em.merge(user);
			// get the role of the lazy mode
			if (user != null)
				user.getNotes();
			em.getTransaction().commit();
			em.close();
			return updatedUser;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}

	public User findByEmail(String email) {
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			User user = (User) em
					.createQuery(" select u from User u Where u.email=:email")
					.setParameter("email", email).getSingleResult();
			// get the role of the lazy mode
			if (user != null)
				user.getNotes();
			em.getTransaction().commit();
			em.close();
			return user;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}

	public User findByNoteId(int id) {
		EntityManager em;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			User user = (User) em
					.createQuery(" select u from Note n INNER JOIN n.user u Where n.id=:id")
					.setParameter("id", id).getSingleResult();
			// get the role of the lazy mode
			if (user != null)
				user.getNotes();
			em.getTransaction().commit();
			em.close();
			return user;
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}
	public void deleteUser(User user) {
		EntityManager em;
		try {
			em = getEntityManager();
			// User tempUser = findByEmail(user.getEmail());
			em.getTransaction().begin();
			em.createQuery("delete from User u where u.email=:email")
					.setParameter("email", user.getEmail()).executeUpdate();
			em.detach(user);
			// get the role of the lazy mode
			if (user != null)
				user.getNotes();
			em.getTransaction().commit();
			em.close();
		} catch (PersistenceException ex) { // to log the exception
			log.log(Level.SEVERE, "unable to getAll " + User.class, ex);
			throw ex;
		}
	}
}
