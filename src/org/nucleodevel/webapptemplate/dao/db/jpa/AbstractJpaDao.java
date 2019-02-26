package org.nucleodevel.webapptemplate.dao.db.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;

/**
 * <p>
 *   Abstract subclass of AbstractDao that implements a default behavior of a DAO that communicates 
 *   with a database via JPA.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 */
public abstract class AbstractJpaDao<E extends AbstractEntity<?>> extends AbstractDao<E> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   Attribute that effectively performs the persistence operations.
	 * </p>
	 */
	private EntityManager entityManager;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
    /**
     * Gets the entityManager from the factory.
     */
    protected EntityManager getEntityManager() {
    	if (entityManager == null) {
    		EntityManagerFactory entityManagerFactory = 
	    		Persistence.createEntityManagerFactory(getPersistenceUnit());
	    	entityManager = entityManagerFactory.createEntityManager();
    	}
		return entityManager;
	}

    /**
     * <p>
     *   A subclass must implement a method that returns the name of the persistence unit that will 
     *   be used by the entityManager.
     * </p>
     */
    protected abstract String getPersistenceUnit();
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Data source read operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectAll()
     */
    @Override
	public List<E> selectAll() {
		return selectAllByNamedQuery("all", null);
	}
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectAllByUniqueParams(java.util.Map)
     */
    @Override
	public List<E> selectAllByUniqueParams(Map<String, Object> params) {
		return selectAllByNamedQuery("one", params);
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectAllByRange(int[])
	 */
	@Override
    public List<E> selectAllByRange(int[] range) {    	
    	TypedQuery<E> q = (TypedQuery<E>) getEntityManager().createNamedQuery(
    		getEntityClass().getSimpleName() + ".findAll", getEntityClass()
    	);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectCount()
     */
    @Override
    public int selectCount() {
        CriteriaQuery<Object> cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<?> rt = cq.from(getEntityClass());
        cq.select((Selection<?>) getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectOne(java.lang.Object)
     */
    @Override
	public E selectOne(Object id) {
		return (E) getEntityManager().find(getEntityClass(), id);
	}

	/**
     * <p>
     *   Returns all entities read from a list of NamedQuery results.
     * </p>
     * @param namedQuery NamedQuery string.
     * @param params Required NamedQuery parameters.
     */
    protected List<E> selectAllByNamedQuery(String namedQuery, Map<String, Object> params) {
    	getEntityManager().getEntityManagerFactory().getCache().evictAll();
    	TypedQuery<E> q = (TypedQuery<E>) getEntityManager().createNamedQuery(
    		getEntityClass().getSimpleName() + "." + namedQuery, getEntityClass()
    	);
        if (params != null)
        	for (Object keyObj: params.keySet()) {
        		String key = (String) keyObj;
        		q.setParameter(key, params.get(key));
        	}
        return q.getResultList();
    }

	/**
     * <p>
     *   Returns the entities read from a list of NamedQuery results that are in a given range.
     * </p>
     * @param namedQuery NamedQuery string.
     * @param params Required NamedQuery parameters.
     * @param limit Number of entities.
     * @param offset First entity.
     */
    protected List<E> selectAllByNamedQuery(
    	String namedQuery, Map<String, Object> params, int limit, int offset 
    ) {
    	getEntityManager().getEntityManagerFactory().getCache().evictAll();
    	TypedQuery<E> q = (TypedQuery<E>) getEntityManager().createNamedQuery(
    		getEntityClass().getSimpleName() + "." + namedQuery, getEntityClass()
    	);
        if (params != null)
        	for (Object keyObj: params.keySet()) {
        		String key = (String) keyObj;
        		q.setParameter(key, params.get(key));
        	}
        return q.setFirstResult(offset).setMaxResults(limit).getResultList();
    }
    
    /**
     * <p>
     *   Returns all entities read from a list of NamedQuery results, and then forces an ordering.
     * </p>
     * @param namedQuery NamedQuery string.
     * @param params Required NamedQuery parameters.
     */
    protected List<E> selectAllByNamedQueryAndSort(String namedQuery, Map<String, Object> params) {
    	return sort(selectAllByNamedQuery(namedQuery, params));
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Data source write operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#insert(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
    public E insert(E entity) {
		getEntityManager().getTransaction().begin();
    	getEntityManager().persist(entity);
	    getEntityManager().getTransaction().commit();
	    return entity;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#update(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
    public E update(E entity) {
    	getEntityManager().getTransaction().begin();
    	getEntityManager().merge(entity);
	    getEntityManager().getTransaction().commit();
	    return entity;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#delete(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
    public E delete(E entity) {
    	getEntityManager().getTransaction().begin();
        getEntityManager().remove(getEntityManager().merge(entity));
	    getEntityManager().getTransaction().commit();   
	    return entity;
    }
    
}