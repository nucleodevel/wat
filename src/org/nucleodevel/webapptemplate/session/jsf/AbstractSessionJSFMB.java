package org.nucleodevel.webapptemplate.session.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um controller de sessão nos sistemas 
 *   que usam webapptemplate. Ela fornece um meio das views acessarem os dados de sessão via a 
 *   classe DAO do tipo AbstractSessionDAO, que fornece esse meio de acessar as variáveis de sessão.
 * </p>
 * <p>
 *   Outro detalhe importante é que esta classe é tipicamente usada como uma Named ViewScoped.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <DAO> classe DAO que apóia este controller.
 */
public abstract class AbstractSessionJSFMB<DAO extends AbstractSessionDAO> 
	implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
    
    /**
	 * <p>
     *   Considere que a classe que estende AbstractSessionContoller seja SC e ela seja parte de um 
     *   sistema (projeto) S. Todo sistema S e controller SC deve ter uma classe DAO que estenda a 
     *   classe AbstractSessionDAO para fornecer os dados de sessão.
     * </p>
	 */
    @Inject
	private DAO dao;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    protected DAO getDao() {
		return dao;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * @return Indica se o dispositivo que está acessando o sistema é móvel. 
	 */
	public boolean isMobile() {
		return getDao().isMobile();
	}
	
	/**
	 * @return Atalho para fornecer a data atual do servidor do sistema.
	 */
	public Date getDate() {
		return new Date();
	}

}