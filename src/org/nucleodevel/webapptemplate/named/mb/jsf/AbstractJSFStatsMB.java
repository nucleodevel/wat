package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.dao.AbstractDAO;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.named.mb.AbstractMB;
import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;
import org.nucleodevel.webapptemplate.util.JSFURLUtils;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean para estatísticas, 
 *   buscas e relatórios, a fim de não poluir o managed bean responsável pelo CRUD da entidade E.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 * @param <DAO> classe DAO que apóia o managed bean.
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o managed bean pertence.
 */
public abstract class AbstractJSFStatsMB
	<E extends AbstractEntity<?>, DAO extends AbstractDAO<E>, SDAO extends AbstractSessionDAO>
	extends AbstractMB<SDAO> 
	implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
    /**
     * <p>
     *   Considere que a classe que estende AbstractMB seja MB. Todo MB deve estar associado a 
     *   uma subclasse de AbstractDAO DAO e ambas, MB e DAO, tenham uma classe entidade E como tipo 
     *   parametrizado. DAO será responsável por efetivamente realizar as operações no datasource  
     *   que MB necessitar.
     * </p>
     */
    @Inject
	private DAO dao;
    
	/**
     * <p>
     *   Atributo que armazena a classe assumida por E, que é a entidade alvo do managed bean. 
     *   Geralmente usado para se obter nome desta classe.
     * </p>
     */
    private Class<E> entityClass;
	
	/**
	 * <p>
	 *   O atributo begin é usado como valor de início em operações que filtram buscas por 
	 *   períodos. Nenhuma operação de período é definida aqui neste managed bean, mas fornece um 
	 *   meio comum de armazenar os atributos de início e fim.
	 * </p>
	 * <p>
     *   Tenta obter o valor inicial de um parâmetro de URL begin.
     * </p>
	 */
	private Date begin;
	
	/**
	 * <p>
	 *   O atributo end é usado como valor de início em operações que filtram buscas por 
	 *   períodos. Nenhuma operação de período é definida aqui neste managed bean, mas fornece um 
	 *   meio comum de armazenar os atributos de início e fim.
	 * </p>
	 * <p>
     *   Tenta obter o valor inicial de um parâmetro de URL end.
     * </p>
	 */
	private Date end;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	protected DAO getDao() {
    	return dao;
	}

    /**
     * <p>
     *   Obtém tipo class do tipo E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils 
    				.getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }
 
	/**
     * <p>
     *   Tenta obter begin via parâmetro URL 'id' ou atribui null.
     * </p>
     */
    public Date getBegin() {
    	if (begin == null)
    		begin = JSFURLUtils.getURLDateParam("begin");
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	/**
     * <p>
     *   Tenta obter end via parâmetro URL 'id' ou atribui null.
     * </p>
     */
    public Date getEnd() {
		if (end == null)
			end = JSFURLUtils.getURLDateParam("end");
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}  
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Permissões
	 * --------------------------------------------------------------------------------------------
	 */
    
	/**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a visualização do resultado de 
     *   uma busca, geralmente expressa em search.jsf
     * </p>
     * @return Permissão para visualização do resultado de uma busca
     */
    public abstract boolean canSearch();
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a visualização do resultado de 
     *   uma relatório, geralmente expresso em report.jsf
     * </p>
     * @return Permissão para visualização do resultado de um relatório
     */
    public abstract boolean canReport();
    
}