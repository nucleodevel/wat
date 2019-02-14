package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;

import org.nucleodevel.webapptemplate.named.mb.AbstractMB;
import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean para buscas.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 * @param <DAO> classe DAO que apóia o controller.
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o controller pertence.
 */
public abstract class AbstractJSFSimpleMB<SDAO extends AbstractSessionDAO>
	extends AbstractMB<SDAO> 
	implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
    
}