package org.nucleodevel.webapptemplate.model.dao.db;

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

import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.model.dao.AbstractDAO;

/**
 * <p>
 *   Subclasse abstrata de AbstractDAO que implementa o comportamento padrão de um DAO que opera 
 *   sobre um datasource do tipo banco de dados.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em uma base de um banco de dados.
 */
public abstract class AbstractDBClient<E extends AbstractEntity<?>> extends AbstractDAO<E> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   Atributo que efetivamente realiza as operações de persistência.
	 * </p>
	 */
	private EntityManager entityManager;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
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
     *   Cada subclasse deve indicar o nome da unidade de persistência que será utilizada por {@link 
     *   #entityManager entityManager}.
     * </p>
     * @return Nome da unidade de persistência que {@link #entityManager entityManager} usa para 
     *   realizar as operações de persistência.
     */
    protected abstract String getPersistenceUnit();
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de leitura de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */
	

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#getAllNow()
     */
    @Override
	public List<E> getAllNow() {
		return getAllByNamedQuery("all", null);
	}
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#getAllByUniqueParams(java.util.Map)
     */
    @Override
	public List<E> getAllByUniqueParams(Map<String, Object> params) {
		return getAllByNamedQuery("one", params);
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#getAllByRange(int[])
	 */
	@Override
    public List<E> getAllByRange(int[] range) {    	
    	TypedQuery<E> q = (TypedQuery<E>) getEntityManager().createNamedQuery(
    		getEntityClass().getSimpleName() + ".findAll", getEntityClass()
    	);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#getCount()
     */
    @Override
    public int getCount() {
        CriteriaQuery<Object> cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<?> rt = cq.from(getEntityClass());
        cq.select((Selection<?>) getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#getOne(java.lang.Object)
     */
    @Override
	public E getOne(Object id) {
		return (E) getEntityManager().find(getEntityClass(), id);
	}

	/**
     * <p>
     *   Retorna todas as entidades de persistência E que foram entregues pela Named Query 
     *   namedQuery com parâmetros definidos em params.
     * </p>
     * @param namedQuery NamedQuery que será invocada.
     * @param params Parâmetros que a nemdQuery espera que sejam declarados.
     * @return Lista com entidades E retornadas pela namedQuery.
     */
    protected List<E> getAllByNamedQuery(String namedQuery, Map<String, Object> params) {
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
     *   Retorna todas as entidades de persistência E que foram entregues pela Named Query 
     *   namedQuery com parâmetros definidos em params e depois ordena-as.
     * </p>
     * @param namedQuery NamedQuery que será invocada.
     * @param params Parâmetros que a nemdQuery espera que sejam declarados.
     * @return Lista ordenada com entidades E retornadas pela namedQuery.
     */
    protected List<E> getAllByNamedQueryAndSort(String namedQuery, Map<String, Object> params) {
    	return sort(getAllByNamedQuery(namedQuery, params));
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de escrita de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#insert(org.nucleodevel.webapptemplate.entity.AbstractEntity)
	 */
	@Override
    public E insert(E entity) {
		getEntityManager().getTransaction().begin();
    	getEntityManager().persist(entity);
	    getEntityManager().getTransaction().commit();
	    return entity;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#update(org.nucleodevel.webapptemplate.entity.AbstractEntity)
	 */
	@Override
    public E update(E entity) {
    	getEntityManager().getTransaction().begin();
    	getEntityManager().merge(entity);
	    getEntityManager().getTransaction().commit();
	    return entity;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#delete(org.nucleodevel.webapptemplate.entity.AbstractEntity)
	 */
	@Override
    public E delete(E entity) {
    	getEntityManager().getTransaction().begin();
        getEntityManager().remove(getEntityManager().merge(entity));
	    getEntityManager().getTransaction().commit();   
	    return entity;
    }
    
}