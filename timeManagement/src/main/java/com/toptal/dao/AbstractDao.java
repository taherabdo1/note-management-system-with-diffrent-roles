package com.toptal.dao;

import java.util.Set;

import javax.persistence.EntityManager;


public interface AbstractDao<T> {
	public T findByID(Integer id);
	public T persist(T entity);
	public T update(T entity);
	public void delete(T entity);
	public Set<T> getAll();
	public void refresh(T entity);
}
