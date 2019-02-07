package org.nucleodevel.webapptemplate.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.model.dao.AbstractDAO;
import org.nucleodevel.webapptemplate.session.AbstractSessionDAO;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;
import org.nucleodevel.webapptemplate.util.PersistAction;
import org.nucleodevel.webapptemplate.util.UrlUtils;
import org.nucleodevel.webapptemplate.util.VariableUtils;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um controller MVC nos sistemas que 
 *   usam webapptemplate. Ela controla o fluxo das operações de CRUD sobre as entidades do tipo E 
 *   e realiza a persistência dessas entidades em conjunto com uma classe DAO do tipo AbstractDAO, 
 *   que pertence ao modelo de dados e efetivamente realiza as operações no datasource. Este 
 *   controller também depende de uma classe DAO de sessão SDAO, que extende AbstractSessionDAO, 
 *   para obter os dados da sessão do sistema ao qual o controller pertence.
 * </p>
 * <p>
 *   Outro detalhe importante é que esta classe é tipicamente usada como uma Named ViewScoped. Isso 
 *   garante que ela é instanciada uma única vez pelo núcleo CDI para cada view que a invocar. Ou 
 *   seja, todo contexto dela é exclusivo para cada view executada e dados alterados por uma view 
 *   não afeta outras views. A opção por ViewScoped facilita o uso de várias abas de navegação e 
 *   edição simultânea de várias entidades, pois o estado da classe não é compartilhado entre as 
 *   views.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 * @param <DAO> classe DAO que apóia o controller.
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o controller pertence.
 */
public abstract class AbstractController
	<E extends AbstractEntity<?>, DAO extends AbstractDAO<E>, SDAO extends AbstractSessionDAO> 
	implements Serializable {
	
	
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
     *   Considere que a classe que estende AbstractContoller seja C, todo C deve estar associado a 
     *   uma subclasse de AbstractDAO DAO e ambas, C e DAO, tenham uma classe entidade E como tipo 
     *   parametrizado. DAO será responsável por efetivamente realizar as operações de CRUD tipo E 
     *   que C necessitar.
     * </p>
     */
    @Inject
	private DAO dao;
	
	/**
	 * <p>
     *   Considere que a classe que estende AbstractContoller seja C e ela seja parte de um sistema 
     *   (projeto) S. Todo sistema S deve implementar uma classe DAO de sessão SDAO que estenda a 
     *   classe AbstractSessionDAO.
     * </p>
	 */
    @Inject
	private SDAO sessionDAO;
    
	/**
     * <p>
     *   Atributo que armazena a classe assumida por E, que é a entidade alvo do controller. 
     *   Geralmente usado para se obter nome desta classe.
     * </p>
     */
    private Class<E> entityClass;

	/**
	 * <p>
	 *   Atributo que mapeia a entidade E, alvo das operações de CRUD. Recebe os valores dos 
	 *   formulários de criação e edição das repectivas views JSF e serve como entidade a ser incida 
	 *   sobre apenas uma entidade E deve usar selected.
	 * </p>  
	 * <p>
	 *   O valor inicial de selected é obtido via parâmetro URL id que mapeia o ID da instância do 
	 *   tipo E. Se não há parâmetro id, selected é nulo.
	 * </p>  
	 */
	private E selected;
	
	/**
	 * <p>
	 *   O atributo begin é usado como valor de início em operações que filtram buscas por períodos. 
	 *   Nenhuma operação de período é definida aqui neste controller, mas fornece um meio comum vez 
	 *   os atributos de início e fim.
	 * </p>
	 * <p>
     *   Tenta obter o valor inicial de um parâmetro de URL begin.
     * </p>
	 */
	private Date begin;
	
	/**
	 * <p>
	 *   O atributo end é usado como valor de fim em operações que filtram buscas por períodos. 
	 *   Nenhuma operação de período é definida aqui neste controller, mas fornece um meio comum vez 
	 *   os atributos de início e fim.
	 * </p>
	 * <p>
     *   Tenta obter o valor inicial de um parâmetro de URL end.
     * </p>
	 */
	private Date end;
	
	/**
	 * <p>
	 *   Atributo que armazena a busca por todos os items do tipo E.
	 * </p>
	 */
	private List<E> all;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	protected DAO getDao() {
    	return dao;
	}
    
    public SDAO getSessionDAO() {
		return sessionDAO;
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
     *   Tenta obter selected via parâmetro URL 'id' ou atribui null.
     * </p>
     */
    public E getSelected() {
		if (selected == null) {
			String idString = UrlUtils.getUrlStringParam("id");
    		
			E newEntity = getDao().getNewEntityInstance();
    		String entityIdClass = newEntity.getEntityIdClass().getSimpleName();
    		
    		if (idString != null && !idString.equals("")) {
	    		if (entityIdClass.equals("Long"))
	    			selected = getDao().getOne(Long.parseLong(idString));
	    		else if (entityIdClass.equals("Integer"))
	    			selected = getDao().getOne(Integer.parseInt(idString));
	    		else if (entityIdClass.equals("Short"))
	    			selected = getDao().getOne(Short.parseShort(idString));
	    		else
	    			selected = getDao().getOne(idString);
    		}
		}
		return selected;
	}

	public void setSelected(E selected) {
		this.selected = selected;
	}
 
	/**
     * <p>
     *   Tenta obter begin via parâmetro URL 'id' ou atribui null.
     * </p>
     */
    public Date getBegin() {
    	if (begin == null)
    		begin = UrlUtils.getUrlDateParam("begin");
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
			end = UrlUtils.getUrlDateParam("end");
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	/**
	 * <p>
	 *   Este método não é um getter propriamente dito, pois ele opera sobre {@link #selected 
	 *   selected}. Se {@link #selected selected} ainda é nulo, é atribuída uma nova instância da 
	 *   classe E. Enquanto getSelected é usada em operações de edição e leitura, getNewSelected é
	 *   usada em operações de criação de entidades E. Portanto, a diferença fundamental destes 2 
	 *   métodos é que newSelected garante a instanciação de uma nova entidade se selected estiver 
	 *   vazio, o que é necessário para iniciar operações de criação. Não é criado um atributo 
	 *   newSelected para que seja possível aproveitar as operações de CRUD já realizadas sobre 
	 *   selected.
	 * </p>
	 * @return Atual valor de {@link #selected selected}.
	 */
	public E getNewSelected() {
		if (selected == null)
			selected = getDao().getNewEntityInstance();
		return selected;
	}

	/**
	 * <p>
	 *   Este método não é um setter propriamente dito, pois ele opera sobre {@link #selected 
	 *   selected}.
	 * </p>
	 * @param selected Novo valor de {@link #selected selected}.
	 */
	public void setNewSelected(E selected) {
		this.selected = selected;
	}

	/**
	 * <p>
	 *   Retorna a lista de todas as entidades E presentes no atributo all sem forçar a releitura.
	 * </p>
	 * @return Lista com todas as entidades E presentes na última leitura feita no datasource.
	 */
	public List<E> getAll() {
		return getAll(false);
	}
	
	/**
	 * <p>
	 *   Se refresh for verdadeiro ou o atributo all for null, força a leitura desta lista no 
	 *   datasource e armazena-a em all; caso contrário, mantém all em seu estado atual, ou seja, 
	 *   all terá a lista da última leitura feita no datasource. Apoś isso, retorna all.
	 * </p>
	 * @return Lista com todas as entidades E presentes no atributo all.
	 */
	public List<E> getAll(boolean refresh) {
		if (all == null || refresh)
			all = getDao().getAll();
    	return all;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Actions
	 * --------------------------------------------------------------------------------------------
	 */
	
	
    /**
     * <p>
     *   Trata a integridade de selected, insere-o no datasource e redireciona à view posterior 
     *   descrita em nextPath.
     *  </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String create(String nextPath) {
    	String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName =  
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			VariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return nextPath != null? nextPath: "index.jsf?faces-redirect=true";
		}
		persist(PersistAction.INSERT, lcClassSimpleName + ".persistence.created");
        return nextPath != null? nextPath: "create.jsf?faces-redirect=true";
    }
	
    /**
     * <p>
     *   Trata a integridade de selected, insere-o no datasource e redireciona à view padrão após a 
     *   operação.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
	public String create() {
		return create(null);
    }

	/**
	 * <p>
     *   Trata a integridade de selected, insere-o no datasource e realiza a operação. O termo Only 
     *   se refere ao fato de que somente a inserção da entidade será realizada, sem qualquer 
     *   redirecionamento a outra view.
     * </p>
     */
	public void createOnly() {
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
        if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			VariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return;
        }
		persist(PersistAction.INSERT, null);
    }
    
	/**
	 * <p>
     *   Trata a integridade de selected, atualiza-o no datasource e redireciona à view posterior 
     *   descrita em nextPath.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String edit(String nextPath) {
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			VariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return nextPath != null? nextPath: "index.jsf?faces-redirect=true";
		}
    	persist(PersistAction.UPDATE, lcClassSimpleName + ".persistence.edited");
    	return nextPath != null? 
    		nextPath: "view.jsf?faces-redirect=true&id=" + selected.getEntityId();
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, atualiza-o no datasource e redireciona à view padrão após 
     *   a operação.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
    public String edit() {
		return edit(null);
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, atualiza-o no datasource e redireciona à view posterior 
     *   descrita em nextPath. A diferença para edit(nextPath) é que nenhuma mensagem de confirmação 
     *   é exibida aqui.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String editWithoutMessage(String nextPath) {
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			VariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return nextPath != null? nextPath: "index.jsf?faces-redirect=true";
		}
    	persist(PersistAction.UPDATE, null);
    	return nextPath != null? 
    		nextPath: "view.jsf?faces-redirect=true&id=" + selected.getEntityId();
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, atualiza-o no datasource e redireciona à view padrão após 
     *   a operação. A diferença para edit() é que nenhuma mensagem de confirmação é exibida aqui.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
    public String editWithoutMessage() {
		return editWithoutMessage(null);
    }

    /**
     * <p>
     *   Trata a integridade de selected, remove-o do datasource e redireciona à view posterior 
     *   descrita em nextPath.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String remove(String nextPath) {  
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		persist(PersistAction.DELETE, lcClassSimpleName + ".persistence.removed");
        selected = null;
        return nextPath != null? nextPath: "/index.jsf?faces-redirect=true";
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, remove-o do datasource e redireciona à view padrão após a 
     *   operação.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
    public String remove() {  
		return remove(null);
    }

    /**
     * <p>
     *   Redireciona as ações de persistência à classe DAO, que realiza as operações solicitadas. 
     *   Verifica o resultado e insere as eventuais mensagens de sucesso ou erro.
     * </p>
     * @param persistAction Especifica qual dos tipos de operação de escrita será realizado: 
     *   inserção, atualização ou remoção. 
     * @param successMessage Mensagem que indica o resultado da operação pretendida.
     */
    private void persist(PersistAction persistAction, String successMessage) {
    	if (selected != null) {
            try {
            	if (persistAction == PersistAction.INSERT)
            		getDao().insert(selected);
            	else if (persistAction == PersistAction.UPDATE)
            		getDao().update(selected);
            	else if (persistAction == PersistAction.DELETE)
            		getDao().delete(selected);
            	if (successMessage != null)
            		VariableUtils.addSuccessMessage(RESOURCE_APP_MSG, successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    VariableUtils.addErrorMessage(RESOURCE_APP_MSG, msg);
                } else {
                	VariableUtils.addErrorMessage(ex, "persistence.error.generic");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
}