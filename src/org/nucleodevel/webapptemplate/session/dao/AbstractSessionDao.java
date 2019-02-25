package org.nucleodevel.webapptemplate.session.dao;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *   Classe abstrata que possibilita ao sistema ter um acesso aos dados de sessão. Portanto, cada 
 *   sistema deve implementar a forma como seus dados de sessão estão estruturados implementando 
 *   esta classe. Trabalha em conjunto com AbstractSessionFilter, que implementa o filtro tomcat 
 *   que verificará a cada requisição se o acesso foi autorizado, seja via login ou outra forma de 
 *   autenticação. Também serve como DAO para o managed bean de sessão e outros managed beans, 
 *   fornecendo os dados de sessão que eles precisam.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionDao {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * @return Atual HttpSession do usuário autenticado
	 */
	protected abstract HttpSession getSession();
	
	/**
	 * @param key nome do atributo a ser lido da sessão HTTP
	 * @return valor do atributo a ser lido da sessão HTTP
	 */
	public Object getSessionAttribute(String key) {
		return getSession().getAttribute(key);
	}
	
	/**
	 * @param key nome do atributo a ser escrito na sessão HTTP
	 * @param value valor a ser escrito no atributo da sessão HTTP 
	 */
	public void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}
	
}