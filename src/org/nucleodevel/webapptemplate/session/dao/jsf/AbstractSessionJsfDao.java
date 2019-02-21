package org.nucleodevel.webapptemplate.session.dao.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.nucleodevel.webapptemplate.session.AbstractSessionDao;
import org.nucleodevel.webapptemplate.util.JsfServletUtils;

/**
 * <p>
 *   Classe abstrata que possibilita ao sistema ter um acesso aos dados de sessão via FacesContext.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionJsfDao extends AbstractSessionDao {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /* (non-Javadoc)
     * @see org.nucleodevel.webapptemplate.session.AbstractSessionDao#getSession()
     */
    protected HttpSession getSession() {
        return 
        	(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
	
	/* (non-Javadoc)
	 * @see org.nucleodevel.webapptemplate.session.AbstractSessionDao#isMobile()
	 */
	public boolean isMobile() {
		return JsfServletUtils.isMobile();
	}
	
}