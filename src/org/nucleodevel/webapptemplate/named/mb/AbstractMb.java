package org.nucleodevel.webapptemplate.named.mb;

import java.io.Serializable;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.session.AbstractSessionDao;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean que tem acesso aos 
 *   dados da sessão atual via o SDAO.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o managed bean pertence.
 */
public abstract class AbstractMb<SDAO extends AbstractSessionDao> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
	/**
	 * <p>
	 *   Caminho padrão para as mensagens customizadas de sistema. Todo sistema deve criar este 
	 *   arquivo e para cada managed bean criar as mensagens necessárias.
	 * </p>
	 */
	protected static final String RESOURCE_APP_MSG = "/resources/app-messages";
	
	/**
	 * <p>
     *   Considere que a classe que estende AbstractMb seja MB e ela seja parte de um sistema 
     *   (projeto) S. Todo sistema S deve implementar uma classe DAO de sessão SDAO que estenda a 
     *   classe AbstractSessionDao.
     * </p>
	 */
    @Inject
	private SDAO sessionDao;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
    
    public SDAO getSessionDao() {
		return sessionDao;
	}
    
}