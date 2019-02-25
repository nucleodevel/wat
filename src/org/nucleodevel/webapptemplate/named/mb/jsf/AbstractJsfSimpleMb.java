package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;

import org.nucleodevel.webapptemplate.named.mb.AbstractMb;
import org.nucleodevel.webapptemplate.session.dao.jsf.AbstractSessionJsfDao;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean simples.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o managed bean pertence.
 */
public abstract class AbstractJsfSimpleMb<SDAO extends AbstractSessionJsfDao>
	extends AbstractMb<SDAO> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
    
}