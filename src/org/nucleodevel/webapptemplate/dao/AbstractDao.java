package org.nucleodevel.webapptemplate.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Abstract class that implements the default behavior of a DAO that provides access to a 
 *   datasource in applications that use webapptemplate. This datasource can be a database or a 
 *   webservice that receives persistence operations.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 */
public abstract class AbstractDao<E extends AbstractEntity<?>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Attribute that stores the class adopted by E, which is the target entity of the DAO. It is
     *   often used to get the name of this class.
     * </p>
     */
    private Class<E> entityClass;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	

    /**
     * <p>
     *   Returns the class<?> of E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils.
    				getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }

	/**
	 * <p>
	 *   Returns a new instance of E via default constructor. Therefore, every class that is 
	 *   adopted by E must have a default constructor without parameters.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public E getNewEntityInstance() {
		try {
			Constructor<?> cons = getEntityClass().getConstructor();
			return (E) cons.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
	 * <p>
	 *   Method used before datasource insertion and update operations to prevent a non-unique 
	 *   entity from being persisted and causing an error. The isInsert flag is required to 
	 *   indicate whether the operation is an insert or update, since the update operation should 
	 *   ignore the entity itself being persisted.
	 * </p>
	 */
	public boolean isAnUniqueEntity(E entity, boolean isInsert) {
		Map<String, Object> params = getUniqueParams(entity);
		if (params != null) {
			List<E> uniqueItems = selectAllByUniqueParams(getUniqueParams(entity));
			if (uniqueItems != null)
				for (E x: uniqueItems)
					if (isInsert || !entity.equals(x))
						return false;
		}
		return true;
	}
    
    /**
     * <p>
     *   A subclass must implement a method that returns the values of the unique attributes of the
     *   entity.
     * </p>
     */
    protected abstract Map<String, Object> getUniqueParams(E entity);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Datasource read operations 
	 * --------------------------------------------------------------------------------------------
	 */


	/**
	 * <p>
	 *   A subclass must implement a method that returns all the entities read from the datasource 
	 *   at a given time.
	 * </p>
	 */
	public abstract List<E> selectAll();
	
	/**
	 * <p>
	 *   A subclass must implement a method that is used by isAnUniqueEntity() to get all entities 
	 *   whose unique attributes values match the values passed by the parameters.
	 * </p>
	 */
	public abstract List<E> selectAllByUniqueParams(Map<String, Object> params);
	
	/**
	 * <p>
	 *   A subclass must implement a method that returns all entities read from a datasource that 
	 *   are in a specific range.
	 * </p>
	 */
	public abstract List<E> selectAllByRange(int[] range);
    
    /**
     * <p>
     *   Returns the number of E entities in the datasource.
     * </p>  
     */
    public int selectCount() {
    	return selectAll().size();
    }

	/**
	 * <p>
	 *   A subclass must implement a method that returns the entity whose ID is passed by 
	 *   parameter.
	 * </p>
	 */
	public abstract E selectOne(Object id);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Datasource write operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/**
	 * <p>
	 *   A subclass must implement a method that performs an insert operation on the entity passed 
	 *   by parameter.
	 * </p>
	 * @return The same entity but with its new ID.
	 */
	public abstract E insert(E entity);

	/**
	 * <p>
	 *   A subclass must implement a method that performs an update operation on the entity passed 
	 *   by parameter.
	 * </p>
	 * @return The same entity.
	 */
	public abstract E update(E entity);

	/**
	 * <p>
	 *   A subclass must implement a method that performs a delete operation on the entity passed 
	 *   by parameter.
	 * </p>
	 * @return The same entity.
	 */
	public abstract E delete(E entity);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Utils
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   A method that can be used to correct a previous operation that returned an unordered 
	 *   collection of E entities. Then this list is returned.
	 * </p>
	 */
	public List<E> sort(List<E> entities) {
		Collections.sort(entities);
		return entities;
	}
    
}