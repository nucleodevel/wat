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
import org.nucleodevel.webapptemplate.dao.AbstractDAO;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;

/**
 * <p>
 *   Subclasse abstrata de AbstractDAO que implementa o comportamento padrão de um DAO que opera 
 *   sobre um datasource do tipo webservice. Na terminologia de webservices, seria um cliente de 
 *   webservice, que obtém os dados de um servidor.   
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade XML ou Json resultante de uma 
 *   operação de webservice.
 */
public abstract class AbstractRESTClient<E extends AbstractEntity<?>> extends AbstractDAO<E> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   Atributo que efetivamente realiza as operações de webservice, fornecendo acesso ao server.
	 * </p>
	 */
	private WebTarget service;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
    protected WebTarget getService() {
    	if (service == null) {
    		ClientConfig config = new ClientConfig();
    		Client client = ClientBuilder.newClient(config).register(MultiPartFeature.class);
    	    service = client.target(UriBuilder.fromUri(getServerURL()).build());
    	}
		return service;
	}
	
	/**
     * <p>
     *   Cada subclasse deve indicar a URL so servidor que provê o webservice. Esta URL será usada 
     *   para iniciar {@link #service service}.
     * </p>
     * @return URL do servidor que provê o webservice.
     */
    public abstract String getServerURL();
	
    /**
     * <p>
     *   Provê o tipo List<E> específico de cada subclasse.
     * </p>
     * @return Tipo List<E> específico de cada subclasse.
     */
    public abstract GenericType<List<E>> getGenericTypeForList();
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de leitura de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */
	

    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#selectAll()
     */
    @Override
	public List<E> selectAll() {
		return 
			getService().request().accept(MediaType.APPLICATION_XML).get(getGenericTypeForList());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#selectAllByRange(int[])
	 */
	@Override
    public List<E> selectAllByRange(int[] range) {    	
    	return null;
    }

	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#selectOne(java.lang.Object)
	 */
	@Override
	public E selectOne(Object id) {
		if (id != null)
			return 
				getService().path(id.toString()).request().accept(MediaType.APPLICATION_XML)
					.get(getEntityClass());
		return null;
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
		Response response = getService().request(MediaType.APPLICATION_XML).post(
			Entity.entity(entity, MediaType.APPLICATION_XML), Response.class
		);
		return response.readEntity(getEntityClass());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#update(org.nucleodevel.webapptemplate.entity.AbstractEntity)
	 */
	@Override
	public E update(E entity) {
		Response response = getService().request(MediaType.APPLICATION_XML).put(
			Entity.entity(entity, MediaType.APPLICATION_XML), Response.class
		);
		return response.readEntity(getEntityClass());
	}
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.model.dao.AbstractDAO#delete(org.nucleodevel.webapptemplate.entity.AbstractEntity)
	 */
	@Override
	public E delete(E entity) {
		Response response =  
			getService().path(entity.getEntityId().toString())
				.request(MediaType.APPLICATION_XML).delete(Response.class);
		return response.readEntity(getEntityClass());
	}
	
}