package org.nucleodevel.webapptemplate.session.dao.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;
import org.nucleodevel.webapptemplate.util.JSFServletUtils;

/**
 * <p>
 *   Classe abstrata que possibilita ao sistema ter um acesso aos dados de sessão via FacesContext.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionJSFDAO extends AbstractSessionDAO {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.session.AbstractSessionDAO#getSession()
     */
    protected HttpSession getSession() {
        return 
        	(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.session.AbstractSessionDAO#isMobile()
	 */
	public boolean isMobile() {
		return JSFServletUtils.isMobile();
	}
	
}