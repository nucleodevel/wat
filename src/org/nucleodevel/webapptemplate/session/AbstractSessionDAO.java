package org.nucleodevel.webapptemplate.session;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.nucleodevel.webapptemplate.util.ServletUtils;

/**
 * <p>
 *   Classe abstrata que possibilita ao sistema ter um acesso aos dados de sessão. Portanto, cada 
 *   sistema deve implementar a forma como seus dados de sessão estão estruturados implementado esta 
 *   super-classe. Trabalha em conjunto com AbstractSessionFilter, que implementa o filtro tomcat 
 *   que verificará a cada requisição se o acesso foi autorizado, seja via login ou outra forma de 
 *   autenticação. Também serve como DAO para o controller de sessão e outros controllers, 
 *   fornecendo os dados de sessão que eles precisem.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionDAO {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * @return Atual HttpSession do usuário autenticado
	 */
	protected HttpSession getSession() {
        return 
        	(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
	
	/**
	 * @return Indica se o dispositivo que está acessando o sistema é móvel. 
	 */
	public boolean isMobile() {
		return ServletUtils.isMobile();
	}
	
}