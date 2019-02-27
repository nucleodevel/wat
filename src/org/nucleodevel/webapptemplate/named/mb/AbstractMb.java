package org.nucleodevel.webapptemplate.named.mb;

import java.io.Serializable;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.session.dao.AbstractSessionDao;

/**
 * <p>
 *   Abstract class that implements the default behavior of a managed bean that retrieves the 
 *   current HTTP session data from an AbstractSessionDao.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <SDAO> DAO class that maps the current HTTP session of the application that owns this 
 *   managed bean.
 */
public abstract class AbstractMb<SDAO extends AbstractSessionDao> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Default path for custom application messages. Each application that uses this library must 
	 *   create this configuration file in which the messages used by the managed beans will be 
	 *   placed.
	 * </p>
	 */
	protected static final String RESOURCE_APP_MSG = "/resources/app-messages";
	
	/**
	 * <p>
     *   SDAO provides access to the current HTTP session data.
     * </p>
	 */
    @Inject
	private SDAO sessionDao;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
    
    public SDAO getSessionDao() {
		return sessionDao;
	}
    
}