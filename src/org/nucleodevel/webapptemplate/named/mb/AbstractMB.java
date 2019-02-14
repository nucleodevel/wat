package org.nucleodevel.webapptemplate.named.mb;

import java.io.Serializable;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean JSF que tem acesso aos
 *   dados da sessão atual via o SDAO.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o controller pertence.
 */
public abstract class AbstractMB<SDAO extends AbstractSessionDAO> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Caminho padrão para as mensagens customizadas de sistema. Todo sistema deve criar este 
	 *   arquivo e para cada controller criar as mensagens necessárias, principalmente as de 
	 *   respostas às operações de CRUD.
	 * </p>
	 */
	protected static final String RESOURCE_APP_MSG = "/resources/app-messages";
	
	/**
	 * <p>
     *   Considere que a classe que estende AbstractContoller seja C e ela seja parte de um sistema 
     *   (projeto) S. Todo sistema S deve implementar uma classe DAO de sessão SDAO que estenda a 
     *   classe AbstractSessionDAO.
     * </p>
	 */
    @Inject
	private SDAO sessionDAO;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
    
    public SDAO getSessionDAO() {
		return sessionDAO;
	}
    
}