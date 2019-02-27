package org.nucleodevel.webapptemplate.session.dao.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.nucleodevel.webapptemplate.session.dao.AbstractSessionDao;

/**
 * <p>
 *   Abstract subclass of AbstractSessionDao that uses JSF FacesContext to provide the current
 *   HTTP session.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionJsfDao extends AbstractSessionDao {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.session.dao.AbstractSessionDao#getSession()
     */
    @Override
	protected HttpSession getSession() {
        return 
        	(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
	
}