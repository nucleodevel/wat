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

import org.nucleodevel.webapptemplate.dao.AbstractDAO;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um servidor que provê um recurso de 
 *   webservice que retorna entidades de um tipo E. Esta classe é considerada um datasource para um 
 *   DAO, que seria o cliente de um web service, do tipo AbstractWSClient. Então, AbstractWSClient é 
 *   o DAO e esta classe o Datasource.   
 * </p>
 * <p>
 *   Na verdade, esta classe é um datasource que redireciona sua requisição para um DAO e seu 
 *   datasource. Ou seja, um AbstractWSServer redireciona suas requisições geralmente para um DAO de 
 *   banco de dados e este para seu datasource. Porém, nada impede que esta classe redirecione para 
 *   outro webservice, porém o mais típico é que o DAO seja de banco de dados, ou seja, um DAO do 
 *   tipo AbstractDbClient. 
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 */
public abstract class AbstractRESTServer
	<TID, E extends AbstractEntity<TID>, DAO extends AbstractDAO<E>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
     * <p>
     *   Considere que a classe que estende AbstractWSServer seja S, todo S deve estar associado a 
     *   uma subclasse de AbstractDAO DAO e ambas, S e DAO, tenham uma classe entidade E como tipo 
     *   parametrizado. DAO será responsável por efetivamente realizar as operações de CRUD tipo E 
     *   que S necessitar.
     * </p>
     */
    protected DAO dao;
    
    /**
     * <p>
     *   Atributo que armazena a classe assumida por DAO, que fornece os dados para este WS Server. 
     *   Geralmente usado para se obter nome desta classe.
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
    		dao = getNewDAOInstance();
		return dao;
	}

    /**
	 * <p>
	 *   Método que retorna uma nova instância do tipo parametrizado DAO via construtor default. Ou 
	 *   seja, toda classe que for assumida por DAO deve ter um construtor padrão sem parâmetros.
	 * </p>
	 * @return Instância da classe parametrizada DAO
	 */
	@SuppressWarnings("unchecked")
	public DAO getNewDAOInstance() {
		try {
			Constructor<?> cons = getDAOClass().getConstructor();   
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
    
	/**
     * <p>
     *   Obtém tipo class do tipo DAO via ParameterizedClassUtils.
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<DAO> getDAOClass() {
    	if (daoClass == null)
    		daoClass = 
    			(Class<DAO>) ParameterizedClassUtils
    				.getParameterClassFromParameterizedClass(getClass(), 2);
    	return daoClass;
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de leitura de dados do datasource
	 * --------------------------------------------------------------------------------------------
	 */
	

    /**
	 * @return Retorna lista de entidades para exibição em browser.
	 */
	@GET
    @Produces(MediaType.TEXT_XML)
    public List<E> getEntitiesBrowser() {
        List<E> entities = getDao().selectAll();
        return entities;
    }

	/**
	 * @return Retorna lista de entidades em formato XML ou Json.
	 */
	@GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<E> getEntities() {
    	List<E> entities = getDao().selectAll();
        return entities;
    }

	/**
	 * @return Retorna número de entidades fornecido pelo DAO.
	 */
	@GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String selectCount() {
        int count = getDao().selectAll().size();
        return String.valueOf(count);
    }
    
	/**
	 * @param id ID da entidade que será retornada. 
	 * @return Retorna entidade cujo ID foi passado por parâmetro em formato XML e Json.
	 */
	@GET
    @Path("{entity}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public E getOne(@PathParam("entity") TID id) {
    	E entity = getDao().selectOne(id);
        return entity;
    }

	/**
	 * @param id ID da entidade que será retornada. 
	 * @return Retorna entidade cujo ID foi passado por parâmetro em formato HTML.
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
	 *   Operações de escrita de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/**
	 * @param jaxbElement Entidade em formato XML que será criada. 
	 * @return Retorna entidade criada em formato XML e Json.
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
	 * @param jaxbElement Entidade em formato XML que será editada. 
	 * @return Retorna entidade editada em formato XML e Json.
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
	 * @param id ID da entidade que será removida.
	 */
	@DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
    	E entity = getDao().selectOne(id);
        getDao().delete(entity);
    }

}