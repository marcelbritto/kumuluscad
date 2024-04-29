
package br.com.marcelbritto.kumuluscad.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jakarta.validation.Valid;

import org.apache.logging.log4j.core.Logger;

import br.com.marcelbritto.kumuluscad.exception.BusinessException;

/**
 * Classe abstrata para os DAOs da aplicação.
 * 
 * @author Marcel Britto
 *
 */
public abstract class AbstractGenericDAO<T, I extends Serializable> {

	private static final String NOME_ARQUIVO_CONSTANTES = "META-INF/ApplicationResources";

	private static ResourceBundle bundle = ResourceBundle.getBundle(NOME_ARQUIVO_CONSTANTES);
	
	protected Class<T> persistedClass;

	public static final String ORDER_ASC  = "ASC";
	public static final String ORDER_DESC = "DESC";

	@Inject
	protected EntityManager em;
	
//	@Inject
//	protected transient Logger logger;
	
	
	protected AbstractGenericDAO() {
	}

	protected AbstractGenericDAO(Class<T> persistedClass) {
		this();
		this.persistedClass = persistedClass;
	}

	public <S extends T> S save(@Valid S entity)  {
		em.persist(entity);
		return entity;
	}

	public <S extends T> S update(@Valid S entity) {
		return em.merge(entity);
	}


	public void delete(@Valid T entity)  {
		
		/*
		 * Para evitar o erro: Entity must be managed to call remove
		 */
		if (!em.contains(entity)) {
			entity = em.merge(entity);
		}
		
		em.remove(entity);
		em.flush();
	}
	
	public void deleteById(final I entityId) {
        final T entity = find(entityId);
        delete(entity);
    }

	public List<T> getList() {
		em.getEntityManagerFactory().getCache().evictAll();
		em.clear();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(persistedClass);
		query.from(persistedClass);
		return em.createQuery(query).getResultList();
	}

	public List<T> getList(String order, String... propertiesOrder) {
		em.getEntityManagerFactory().getCache().evictAll();
		em.clear();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(persistedClass);
		Root<T> root = cq.from(persistedClass);

		List<javax.persistence.criteria.Order> orders = new ArrayList<>();
		for (String propertyOrder : propertiesOrder) {
			if (order.equals(ORDER_ASC)) {
				orders.add(cb.asc(root.get(propertyOrder)));
			} else {
				orders.add(cb.desc(root.get(propertyOrder)));
			}
		}
		cq.orderBy(orders);

		return em.createQuery(cq).getResultList();
	}

	public T find(I id) {
		if (id == null) return null;
		em.getEntityManagerFactory().getCache().evictAll();
		em.clear();
		em.flush();

		T obj = em.find(persistedClass, id);
		// Para evitar a exception: java.lang.IllegalArgumentException: Cannot refresh unmanaged object: null
		if (obj != null) {
			em.refresh(obj);
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	public T getObject(String namedQuery, Object id) {
		return (T) em.createNamedQuery(namedQuery)
				.setParameter("id", id)
				.getSingleResult();
	}

	/**
	 * Retorna um Objeto de acordo com a NamedQuery, único parâmetro e único
	 * valor passado.
	 * 
	 * @param namedQuery
	 * @param parameter
	 * @param value
	 * @return
	 */
	public T getSingleObject(String namedQuery, String parameter, Object value) throws BusinessException {
		em.getEntityManagerFactory().getCache().evictAll();
		em.clear();
		Query query = em.createNamedQuery(namedQuery);
		if (parameter != null && value != null) {
			query.setParameter(parameter, value);
		}
		
		try {  //não seria mais fácil fazer assim do que do jeito que tá embaixo?
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new BusinessException(bundle.getString("error_more_than_one"));
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<T> getObjects(String namedQuery, String[] parameters, Object[] values) {
		em.getEntityManagerFactory().getCache().evictAll();
		em.clear();
		Query query = em.createNamedQuery(namedQuery);
		if (parameters != null && values != null && (parameters.length == values.length)) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(parameters[i], values[i]);
			}
		}
		var list = query.getResultList();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					try {
						em.refresh(list.get(i));
					} catch (IllegalArgumentException | TransactionRequiredException ex) {
						return list;
					}
				}
			}
			return list;
		}
		return null;
	}
	
	
}
