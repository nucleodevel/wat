package org.nucleodevel.webapptemplate.session.dao;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *   Abstract class that implements the default behavior of a DAO that provides access to the 
 *   current HTTP session. It is intended for use by managed beans that use this library.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionDao {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * Returns the current HTTP session.
	 */
	protected abstract HttpSession getSession();
	
	/**
	 * Returns the attribute of the current HTTP session whose key is passed by parameter.
	 */
	public Object getSessionAttribute(String key) {
		return getSession().getAttribute(key);
	}
	
	/**
	 * Sets the attribute of the current HTTP session whose key and value are passed by parameter.
	 */
	public void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	
}