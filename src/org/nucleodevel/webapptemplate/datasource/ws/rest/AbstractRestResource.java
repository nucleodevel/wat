package org.nucleodevel.webapptemplate.datasource.ws.rest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Abstract class that implements the default behavior of a REST webservice resource that 
 *   provides operations of entities E to an AbstractRestClient.   
 * </p>
 * <p>
 *   Since a Web service resource typically needs a DAO to access a database or other data source, 
 *   it must indicate which subclass of AbstractDao will be its DAO.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 */
public abstract class AbstractRestResource
	<TID, E extends AbstractEntity<TID>, DAO extends AbstractDao<E>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
     * <p>
     *   Since a Web service resource typically needs a DAO to access a database or other data 
     *   source, it must indicate which subclass of AbstractDao will be its DAO.
     * </p>
     */
    protected DAO dao;
    
    /**
     * <p>
     *   Attribute that stores the class adopted by DAO that provides access to the data source 
     *   used by this REST resource.
     * </p>
     */
    private Class<DAO> daoClass;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
    protected DAO getDao() {
    	if (dao == null)
    		dao = getNewDaoInstance();
		return dao;
	}
    
	/**
     * <p>
     *   Returns the class<?> of DAO via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<DAO> getDaoClass() {
    	if (daoClass == null)
    		daoClass = 
    			(Class<DAO>) ParameterizedClassUtils
    				.getParameterClassFromParameterizedClass(getClass(), 2);
    	return daoClass;
    }

    /**
	 * <p>
	 *   Returns a new instance of DAO via default constructor. Therefore, every class that is 
	 *   adopted by DAO must have a default constructor without parameters.
	 * </p>
	 * @return Instância da classe parametrizada DAO
	 */
	@SuppressWarnings("unchecked")
	public DAO getNewDaoInstance() {
		try {
			Constructor<?> cons = getDaoClass().getConstructor();   
			return (DAO) cons.newInstance();
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
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de leitura de dados do datasource
	 * --------------------------------------------------------------------------------------------
	 */
	

    /**
	 * Returns a list of all E entities to display in a browser.
	 */
	@GET
    @Produces(MediaType.TEXT_XML)
    public List<E> getEntitiesBrowser() {
        List<E> entities = getDao().selectAll();
        return entities;
    }

	/**
	 * Returns a list of all E entities in XML and JSON formats.
	 */
	@GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<E> getEntities() {
    	List<E> entities = getDao().selectAll();
        return entities;
    }

	/**
	 * Returns the number of existing E entities.
	 */
	@GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        int count = getDao().selectAll().size();
        return String.valueOf(count);
    }
    
	/**
	 * Returns a specific E entity by the ID passed by parameter. 
	 */
	@GET
    @Path("{entity}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public E getOne(@PathParam("entity") TID id) {
    	E entity = getDao().selectOne(id);
        return entity;
    }

	/**
	 * Returns a specific E entity by the ID passed by parameter in HTML format.
	 */
	@GET
    @Path("{entity}")
    @Produces(MediaType.TEXT_XML)
    public E getOneHTML(@PathParam("entity") TID id) {
        E entity = getDao().selectOne(id);
        return entity;
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Data source write operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/**
	 * Prompts DAO to perform an insert operation on the entity passed by parameter. 
	 * @return The same entity with its new ID.
	 */
	@POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_XML)
    public E create(JAXBElement<E> jaxbElement) {
        E entity = jaxbElement.getValue();
        getDao().insert(entity);
        return entity;
    }

	/**
	 * Prompts DAO to perform an update operation on the entity passed by parameter. 
	 * @return The same entity.
	 */
	@PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_XML)
    public E edit(JAXBElement<E> jaxbElement) {
    	E entity = jaxbElement.getValue();
        getDao().update(entity);
        return entity;
    }
    
	/**
	 * Prompts DAO to perform a delete operation on the entity passed by parameter. 
	 */
	@DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
    	E entity = getDao().selectOne(id);
        getDao().delete(entity);
    }

}