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
 *   Subclasse abstrata de AbstractDao que implementa o comportamento padrão de um DAO que opera 
 *   sobre um datasource que é um banco de dados acessado via JPA.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em uma base de um banco de dados.
 */
public abstract class AbstractJpaDao<E extends AbstractEntity<?>> extends AbstractDao<E> {
	
	
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
	
    
    /**
     * Na primeira requisição, obtém o entityManager.
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
     *   Cada subclasse deve indicar o nome da unidade de persistência que será utilizada por 
     *   {@link #entityManager entityManager}.
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
     *   Retorna todas as entidades de persistência E que foram entregues pela Named Query 
     *   namedQuery com parâmetros definidos em params.
     * </p>
     * @param namedQuery NamedQuery que será invocada.
     * @param params Parâmetros que a nemdQuery espera que sejam declarados.
     * @return Lista com entidades E retornadas pela namedQuery.
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
     *   Retorna todas as entidades de persistência E que foram entregues pela Named Query 
     *   namedQuery com parâmetros definidos em params.
     * </p>
     * @param namedQuery NamedQuery que será invocada.
     * @param params Parâmetros que a nemdQuery espera que sejam declarados.
     * @param limit Número de entidades retornadas.
     * @param offset Primeira entidade retornada.
     * @return Lista com entidades E retornadas pela namedQuery.
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
     *   Retorna todas as entidades de persistência E que foram entregues pela Named Query 
     *   namedQuery com parâmetros definidos em params e depois ordena-as.
     * </p>
     * @param namedQuery NamedQuery que será invocada.
     * @param params Parâmetros que a nemdQuery espera que sejam declarados.
     * @return Lista ordenada com entidades E retornadas pela namedQuery.
     */
    protected List<E> selectAllByNamedQueryAndSort(String namedQuery, Map<String, Object> params) {
    	return sort(selectAllByNamedQuery(namedQuery, params));
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de escrita de dados no datasource 
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