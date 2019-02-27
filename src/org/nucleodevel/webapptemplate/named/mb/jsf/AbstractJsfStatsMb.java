package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.named.mb.AbstractMb;
import org.nucleodevel.webapptemplate.session.dao.jsf.AbstractSessionJsfDao;
import org.nucleodevel.webapptemplate.util.JsfUrlUtils;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Abstract subclass of AbstractMb that implements the default behavior of a managed bean for 
 *   statistics, queries, and reports, in order not to pollute the subclasses of AbstractJsfCrudMb.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 * @param <DAO> DAO class that provides access to the datasource.
 * @param <SDAO> DAO class that maps the current HTTP session of the application that owns this 
 *   managed bean.
 */
public abstract class AbstractJsfStatsMb
	<E extends AbstractEntity<?>, DAO extends AbstractDao<E>, SDAO extends AbstractSessionJsfDao>
	extends AbstractMb<SDAO> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
    /**
     * <p>
     *   DAO provides access to the datasource used by the managed bean.
     * </p>
     */
    @Inject
	private DAO dao;
    
    /**
     * <p>
     *   Attribute that stores the class adopted by E, which is the target entity of the DAO. It is
     *   often used to get the name of this class.
     * </p>
     */
    private Class<E> entityClass;
	
	/**
	 * <p>
	 *   Used as the beggining date for searches. No operations are defined here, but begin can be 
	 *   used in subclasses in your searches. Begin must be started by an HTTP GET parameter with 
	 *   the same name.
	 * </p>
	 */
	private Date begin;
	
	/**
	 * <p>
	 *   Used as the end date for searches. No operation is defined here, but the end can be used 
	 *   in subclasses in your searches. End must be started by an HTTP GET parameter with the same 
	 *   name.
	 * </p>
	 */
	private Date end;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	protected DAO getDao() {
    	return dao;
	}

    /**
     * <p>
     *   Returns the class<?> of E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils 
    				.getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }
 
	/**
     * <p>
     *   Attempts to get the attribute begin by the HTTP GET parameter with the same name or define 
     *   null.
     * </p>
     */
    public Date getBegin() {
    	if (begin == null)
    		begin = JsfUrlUtils.getUrlDateParam("begin");
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	/**
     * <p>
     *   Attempts to get the attribute end by the HTTP GET parameter with the same name or define 
     *   null.
     * </p>
     */
    public Date getEnd() {
		if (end == null)
			end = JsfUrlUtils.getUrlDateParam("end");
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}  
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   JSF View Filters
	 * --------------------------------------------------------------------------------------------
	 */
    
	/**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the search of entities. 
     *   Intended to be used primarily in search.jsf, but can be used in other JSF views.
     * </p>
     * @return Permissão para visualização do resultado de uma busca
     */
    public abstract boolean canSearch();
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the report of entities. 
     *   Intended to be used primarily in report.jsf, but can be used in other JSF views.
     * </p>
     * @return Permissão para visualização do resultado de um relatório
     */
    public abstract boolean canReport();
    
}