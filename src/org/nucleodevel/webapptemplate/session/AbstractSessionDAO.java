package org.nucleodevel.webapptemplate.session;

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
public abstract class AbstractSessionDAO {
	
	
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
	 * @return Indica se o dispositivo que está acessando o sistema é móvel. 
	 */
	public abstract boolean isMobile();
	
}