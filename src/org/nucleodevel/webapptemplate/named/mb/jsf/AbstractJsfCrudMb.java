package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.named.mb.AbstractMb;
import org.nucleodevel.webapptemplate.session.AbstractSessionDao;
import org.nucleodevel.webapptemplate.util.JsfUrlUtils;
import org.nucleodevel.webapptemplate.util.JsfVariableUtils;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;
import org.nucleodevel.webapptemplate.util.PersistAction;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um managed bean MVC nos sistemas que 
 *   usam webapptemplate. Ela controla o fluxo das operações de CRUD sobre as entidades do tipo E 
 *   e realiza a persistência dessas entidades em conjunto com uma classe DAO do tipo AbstractDao, 
 *   que pertence ao modelo de dados e efetivamente realiza as operações no datasource. Este 
 *   managed bean também depende de uma classe DAO de sessão SDAO, que extende AbstractSessionDao, 
 *   para obter os dados da sessão do sistema ao qual o managed bean pertence.
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
 * @param <DAO> classe DAO que apóia o managed bean.
 * @param <SDAO> classe DAO que mapeia a sessão do sistema ao qual o managed bean pertence.
 */
public abstract class AbstractJsfCrudMb
	<E extends AbstractEntity<?>, DAO extends AbstractDao<E>, SDAO extends AbstractSessionDao>
	extends AbstractMb<SDAO> 
	implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
    /**
     * <p>
     *   Considere que a classe que estende AbstractMb seja MB. Todo MB deve estar associado a 
     *   uma subclasse de AbstractDao DAO e ambas, MB e DAO, tenham uma classe entidade E como tipo 
     *   parametrizado. DAO será responsável por efetivamente realizar as operações de CRUD tipo E 
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
	 *   Atributo que mapeia a entidade E, alvo das operações de CRUD. Recebe os valores dos 
	 *   formulários de criação e edição das repectivas views JSF e serve como entidade a ser 
	 *   persistida no DAO.
	 * </p>  
	 * <p>
	 *   O valor inicial de selected é obtido via parâmetro URL id que mapeia o ID da instância do 
	 *   tipo E. Se não há parâmetro id, selected é nulo.
	 * </p>  
	 */
	private E selected;
	
	/**
	 * <p>
	 *   Atributo que armazena a busca por todos os items do tipo E.
	 * </p>
	 */
	protected List<E> all;
	
	
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
     *   Tenta obter selected via parâmetro URL 'id' ou atribui null.
     * </p>
     */
    public E getSelected() {
		if (selected == null) {
			String idString = JsfUrlUtils.getUrlStringParam("id");
    		
			E newEntity = getDao().getNewEntityInstance();
    		String entityIdClass = newEntity.getEntityIdClass().getSimpleName();
    		
    		if (idString != null && !idString.equals("")) {
	    		if (entityIdClass.equals("Long"))
	    			selected = getDao().selectOne(Long.parseLong(idString));
	    		else if (entityIdClass.equals("Integer"))
	    			selected = getDao().selectOne(Integer.parseInt(idString));
	    		else if (entityIdClass.equals("Short"))
	    			selected = getDao().selectOne(Short.parseShort(idString));
	    		else
	    			selected = getDao().selectOne(idString);
    		}
		}
		return selected;
	}

	public void setSelected(E selected) {
		this.selected = selected;
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
	 *   Retorna a lista de todas as entidades E presentes para o atributo all sem forçar a 
	 *   releitura.
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
	 *   all terá a lista da última leitura feita no datasource. Após isso, retorna all.
	 * </p>
	 * @return Lista com todas as entidades E presentes no atributo all.
	 */
	public List<E> getAll(boolean refresh) {
    	if (!canViewAll())
    		return null;
    	
		if (all == null || refresh)
			all = getDao().selectAll();
    	return all;
	}
	
    public void setAll(List<E> all) {
		this.all = all;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Actions
	 * --------------------------------------------------------------------------------------------
	 */
	

	/**
     * <p>
     *   Trata a integridade de selected, solicita ao DAO que insira no datasource e redireciona à 
     *   view posterior descrita em nextPath.
     *  </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String create(String nextPath) {
    	if (!canCreate()) {
    		JsfVariableUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
    	String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName =  
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			JsfVariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return nextPath != null? nextPath: "index.jsf?faces-redirect=true";
		}
		persist(PersistAction.INSERT, lcClassSimpleName + ".persistence.created");
        return nextPath != null? nextPath: "create.jsf?faces-redirect=true";
    }
	
    /**
     * <p>
     *   Trata a integridade de selected, solicita ao DAO que insira no datasource e redireciona à 
     *   view padrão após a operação.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
	public String create() {
		return create(null);
    }

	/**
	 * <p>
     *   Trata a integridade de selected, solicita ao DAO que insira no datasource e realiza a 
     *   operação. O termo Only se refere ao fato de que somente a inserção da entidade será 
     *   realizada, sem qualquer redirecionamento a outra view.
     * </p>
     */
	public void createOnly() {
    	if (!canCreate()) {
    		JsfVariableUtils.addErrorMessage("can.error.generic");
			return;
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
        if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			JsfVariableUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return;
        }
		persist(PersistAction.INSERT, null);
    }
    
	/**
	 * <p>
     *   Trata a integridade de selected, solicita ao DAO que atualize no datasource e redireciona 
     *   à view posterior descrita em nextPath.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String edit(String nextPath) {
    	if (!canEdit()) {
    		JsfVariableUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			JsfVariableUtils.addErrorMessage(
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
     *   Trata a integridade de selected, solicita ao DAO que atualize no datasource e redireciona 
     *   à view padrão após a operação.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
    public String edit() {
		return edit(null);
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, solicita ao DAO que atualize no datasource e redireciona 
     *   à view posterior descrita em nextPath. A diferença para edit(nextPath) é que nenhuma 
     *   mensagem de confirmação é exibida aqui.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String editWithoutMessage(String nextPath) {
    	if (!canEdit()) {
    		JsfVariableUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			JsfVariableUtils.addErrorMessage(
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
     *   Trata a integridade de selected, solicita ao DAO que atualize no datasource e redireciona 
     *   à view padrão após a operação. A diferença para edit() é que nenhuma mensagem de 
     *   confirmação é exibida aqui.
     * </p>
     * @return caminho padrão de redirecionamento. O ciclo de vida do JSF espera que seja retornada 
     *   uma String com este caminho.
     */
    public String editWithoutMessage() {
		return editWithoutMessage(null);
    }

    /**
     * <p>
     *   Trata a integridade de selected, solicita ao DAO que remova no datasource e redireciona à 
     *   view posterior descrita em nextPath.
     * </p>
     * @param nextPath caminho para o qual a ação será direcionada após a operação de persistência.
     * @return nextPath ou o caminho padrão por omissão. O ciclo de vida do JSF espera que seja 
     *   retornada uma String com este caminho.
     */
    public String remove(String nextPath) {  
    	if (!canRemove()) {
    		JsfVariableUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		persist(PersistAction.DELETE, lcClassSimpleName + ".persistence.removed");
        selected = null;
        return nextPath != null? nextPath: "/index.jsf?faces-redirect=true";
    }
    
    /**
     * <p>
     *   Trata a integridade de selected, solicita ao DAO que remova no datasource e redireciona à 
     *   view padrão após a operação.
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
            		JsfVariableUtils.addSuccessMessage(RESOURCE_APP_MSG, successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfVariableUtils.addErrorMessage(RESOURCE_APP_MSG, msg);
                } else {
                	JsfVariableUtils.addErrorMessage(ex, "persistence.error.generic");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Permissões
	 * --------------------------------------------------------------------------------------------
	 */
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a visualização das entidades 
     *   armazenadas em all. Destinado a ser usado principalmente nas views JSF.
     * </p>
     * @return Permissão para visualização das entidades armazenadas em all
     */
    public abstract boolean canViewAll();
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a visualização de uma entidade
     *   específica do tipo E. Destinado a ser usado principalmente nas views JSF.
     * </p>
     * @return Permissão para visualização de uma entidade específica do tipo E
     */
    public abstract boolean canView(E selected);
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a criação da entidade 
     *   newSelected. Destinado a ser usado principalmente nas views JSF, mas é usado também nos 
     *   métodos create.
     * </p>
     * @return Permissão para criação de newSelected
     */
    public abstract boolean canCreate();
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a edição de uma entidade
     *   específica do tipo E. Destinado a ser usado principalmente nas views JSF.
     * </p>
     * @return Permissão para edição de uma entidade específica do tipo E
     */
    public abstract boolean canEdit(E selected);
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a remoção de uma entidade
     *   específica do tipo E. Destinado a ser usado principalmente nas views JSF.
     * </p>
     * @return Permissão para remoção de uma entidade específica do tipo E
     */
    public abstract boolean canRemove(E selected);
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a visualização de selected. 
     *   Destinado a ser usado principalmente nas views JSF.
     * </p>
     * @return Permissão para visualização de selected
     */
    public boolean canView() {
    	return canView(getSelected());
    }
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a edição de selected. 
     *   Destinado a ser usado principalmente nas views JSF, mas é usado também nos métodos edit.
     * </p>
     * @return Permissão para edição de selected
     */
    public boolean canEdit() {
    	return canEdit(getSelected());
    }
    
    /**
     * <p>
     *   Filtro que possibilita o managed bean permitir ou proibir a remoção de selected. 
     *   Destinado a ser usado principalmente nas views JSF, mas é usado também nos métodos remove.
     * </p>
     * @return Permissão para remoção de selected
     */
    public boolean canRemove() {
    	return canRemove(getSelected());
    }
    
}