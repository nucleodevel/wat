package org.nucleodevel.webapptemplate.session.dao.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.nucleodevel.webapptemplate.session.dao.AbstractSessionDao;

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
    @Override
	protected HttpSession getSession() {
        return 
        	(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
	
}