package org.nucleodevel.webapptemplate.dao.ws.rest;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;

/**
 * <p>
 *   Abstract subclass of AbstractDao that implements a default behavior of a DAO that communicates 
 *   with a REST webservice resource, which means that it is a REST webservice client that consumes 
 *   data from that resource.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an XML or JSON entity resulting from a 
 *   webservice operation.
 */
public abstract class AbstractRestClient<E extends AbstractEntity<?>> extends AbstractDao<E> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   Attribute that effectively performs the REST webservice operations by providing access to 
	 *   the resource.
	 * </p>
	 */
	private WebTarget resource;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
    protected WebTarget getResource() {
    	if (resource == null) {
    		ClientConfig config = new ClientConfig();
    		Client client = ClientBuilder.newClient(config).register(MultiPartFeature.class);
    	    resource = client.target(UriBuilder.fromUri(getResourceUrl()).build());
    	}
		return resource;
	}
	
	/**
     * <p>
     *   A subclass must implement a method that returns the URL of the REST resource. Used by 
     *   getResource().
     * </p>
     */
    public abstract String getResourceUrl();
	
    /**
     * <p>
     *   A subclass must implement a method that indicates the specific type of List for E 
     *   entities.
     * </p>
     */
    public abstract GenericType<List<E>> getGenericTypeForList();
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Datasource read operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectAll()
     */
    @Override
	public List<E> selectAll() {
		return 
			getResource().request().accept(MediaType.APPLICATION_XML).get(getGenericTypeForList());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectAllByRange(int[])
	 */
	@Override
    public List<E> selectAllByRange(int[] range) {    	
    	return null;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#selectOne(java.lang.Object)
	 */
	@Override
	public E selectOne(Object id) {
		if (id != null)
			return 
				getResource().path(id.toString()).request().accept(MediaType.APPLICATION_XML)
					.get(getEntityClass());
		return null;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Datasource write operations 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#insert(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
	public E insert(E entity) {
		Response response = getResource().request(MediaType.APPLICATION_XML).post(
			Entity.entity(entity, MediaType.APPLICATION_XML), Response.class
		);
		return response.readEntity(getEntityClass());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#update(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
	public E update(E entity) {
		Response response = getResource().request(MediaType.APPLICATION_XML).put(
			Entity.entity(entity, MediaType.APPLICATION_XML), Response.class
		);
		return response.readEntity(getEntityClass());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.dao.AbstractDao#delete(
	 *     org.nucleodevel.webapptemplate.entity.AbstractEntity
	 * )
	 */
	@Override
	public E delete(E entity) {
		Response response =  
			getResource().path(entity.getEntityId().toString())
				.request(MediaType.APPLICATION_XML).delete(Response.class);
		return response.readEntity(getEntityClass());
	}
	
}