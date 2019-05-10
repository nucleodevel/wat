package org.nucleodevel.webapptemplate.session;

import javax.servlet.Filter;

/**
 * <p>
 *   Classe abstrata usada como filtro tomcat que permite a verificação de itens necessários à 
 *   sessão, como teste de login e autorizações. Trabalha em conjunto com AbstractSessionDAO, que 
 *   possibilita ao sistema ter um acesso aos dados de sessão.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public abstract class AbstractSessionFilter implements Filter {
	
}