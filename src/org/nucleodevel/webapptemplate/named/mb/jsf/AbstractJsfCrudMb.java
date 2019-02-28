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
import org.nucleodevel.webapptemplate.session.dao.jsf.AbstractSessionJsfDao;
import org.nucleodevel.webapptemplate.util.JsfMessageUtils;
import org.nucleodevel.webapptemplate.util.JsfUrlUtils;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;
import org.nucleodevel.webapptemplate.util.PersistAction;

/**
 * <p>
 *   Abstract subclass of AbstractMb that implements the default behavior of a managed bean 
 *   JSF that simulates a CRUD scenario with many JSF views. It controls the CRUD flow of an E 
 *   entity called selected and uses DAO to persist it. Subclasses must be declared as ViewScoped 
 *   because the selected is passed between views by ID via HTTP GET and this enables the use of 
 *   multiple tabs in the browser to work with several different CRUD scenarios and their 
 *   respective "selecteds".
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 * @param <DAO> DAO class that provides access to the datasource.
 * @param <SDAO> DAO class that maps the current HTTP session of the application that owns this 
 *   managed bean.
 */
public abstract class AbstractJsfCrudMb
	<E extends AbstractEntity<?>, DAO extends AbstractDao<E>, SDAO extends AbstractSessionJsfDao>
	extends AbstractMb<SDAO> 
	implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
    /**
     * <p>
     *   DAO provides access to the datasource used by the managed bean.
     * </p>
     */
    @Inject
	private DAO dao;
    
	/**
     * <p>
     *   Attribute that stores the class adopted by E, which is the target entity of the DAO. It is
     *   often used to get the name of this class.
     * </p>
     */
    private Class<E> entityClass;

	/**
	 * <p>
	 *   Target entity of CRUD operations between JSF views. It receives the values of its 
	 *   attributes ​​from JSF forms and is the entity that will be persisted by DAO. Selected is 
	 *   created according to the value passed by the ID via HTTP GET or, if there is no ID, 
	 *   considered null.
	 * </p>  
	 */
	protected E selected;
	
	/**
	 * <p>
	 *   The attribute that stores the list that contains all the E entities in the datasource.
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
     *   Returns the class<?> of E via ParameterizedClassUtils
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
     *   Selected is deduced by the ID passed by HTTP GET or is considered null.
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
	 *   It is a pseudo getter used to force selected not to be null. It is used in scenarios where 
	 *   the selected is the target of an entity create operation. Therefore getSelected() must 
	 *   be used by edit operations and getNewSelected() must be used by create operations.
	 * </p>
	 * @return Atual valor de {@link #selected selected}.
	 */
	public E getNewSelected() {
		if (selected == null)
			selected = getDao().getNewEntityInstance();
		return selected;
	}

	public void setNewSelected(E selected) {
		this.selected = selected;
	}

	public List<E> getAll() {
		return getAll(false);
	}
	
	/**
	 * <p>
	 *   Returns the list of all E entities in the datasource at a given time, overwriting the last 
	 *   read if the parameter is true. Uses canAll(), implemented by subclasses, as a permission 
	 *   filter.
	 * </p>
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
     *   Tests canCreate() as the permission filter and does an insert operation for selected. 
     *   Returns the string with the path of the next view.
     *  </p>
     */
    public String create(String nextPath) {
    	if (!canCreate()) {
    		JsfMessageUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
    	String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName =  
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			JsfMessageUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return nextPath != null? nextPath: "index.jsf?faces-redirect=true";
		}
		persist(PersistAction.INSERT, lcClassSimpleName + ".persistence.created");
        return nextPath != null? nextPath: "create.jsf?faces-redirect=true";
    }
	
    /**
     * <p>
     *   Tests canCreate() as the permission filter and does an insert operation for selected,
     *   but continues in the same view.
     * </p>
     */
	public String create() {
		return create(null);
    }

	/**
	 * <p>
     *   Tests canCreate() as the permission filter and does an insert operation for selected.
     * </p>
     */
	public void createOnly() {
    	if (!canCreate()) {
    		JsfMessageUtils.addErrorMessage("can.error.generic");
			return;
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
        if (!getDao().isAnUniqueEntity(getSelected(), true)) {
			JsfMessageUtils.addErrorMessage(
				RESOURCE_APP_MSG, lcClassSimpleName + ".persistence.unique.error"
			);
			return;
        }
		persist(PersistAction.INSERT, null);
    }
    
	/**
	 * <p>
     *   Tests canEdit() as the permission filter and does an update operation for selected.
     *   Returns the string with the path of the next view.
     * </p>
     */
    public String edit(String nextPath) {
    	if (!canEdit()) {
    		JsfMessageUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			JsfMessageUtils.addErrorMessage(
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
     *   Tests canEdit() as the permission filter and does an update operation for selected,
     *   but continues in the same view.
     * </p>
     */
    public String edit() {
		return edit(null);
    }
    
    /**
     * <p>
     *   Tests canEdit() as the permission filter and does an update operation for selected,
     *   returns the string with the path of the next view but does not display any messages.
     * </p>
     */
    public String editWithoutMessage(String nextPath) {
    	if (!canEdit()) {
    		JsfMessageUtils.addErrorMessage("can.error.generic");
			return "";
    	}
    	
		String classSimpleName = getEntityClass().getSimpleName();
    	String lcClassSimpleName = 
        	Character.toLowerCase(classSimpleName.charAt(0)) + classSimpleName.substring(1);
    	
		if (!getDao().isAnUniqueEntity(getSelected(), false)) {
			JsfMessageUtils.addErrorMessage(
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
     *   Tests canEdit() as the permission filter and does an update operation for selected, 
     *   but continues in the same view and does not display any messages.
     * </p>
     */
    public String editWithoutMessage() {
		return editWithoutMessage(null);
    }

    /**
     * <p>
     *   Tests canRemove() as the permission filter and does a delete operation for selected. 
     *   Returns the string with the path of the next view.
     * </p>
     */
    public String remove(String nextPath) {  
    	if (!canRemove()) {
    		JsfMessageUtils.addErrorMessage("can.error.generic");
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
     *   Tests canRemove() as the permission filter and does a delete operation for selected, 
     *   but continues in the same view.
     * </p>
     */
    public String remove() {  
		return remove(null);
    }

    /**
     * <p>
     *   Generic method that effectively performs persistence operations.
     * </p>
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
            		JsfMessageUtils.addSuccessMessage(RESOURCE_APP_MSG, successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfMessageUtils.addErrorMessage(RESOURCE_APP_MSG, msg);
                } else {
                	JsfMessageUtils.addErrorMessage(ex, "persistence.error.generic");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   JSF View Filters
	 * --------------------------------------------------------------------------------------------
	 */
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the viewing of entities stored 
     *   in "all". Intended to be used primarily in view-all.jsf, but can be used in other JSF 
     *   views.
     * </p>
     */
    public abstract boolean canViewAll();
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the viewing of a specific E 
     *   entity. Intended to be used primarily in JSF views.
     * </p>
     */
    public abstract boolean canView(E selected);
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the creation of "selected". 
     *   Intended to be used primarily in create.jsf, but can be used in other JSF views.
     * </p>
     */
    public abstract boolean canCreate();
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the editing of a specific E 
     *   entity. Intended to be used primarily in JSF views.
     * </p>
     */
    public abstract boolean canEdit(E selected);
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the removing of a specific E 
     *   entity. Intended to be used primarily in JSF views.
     * </p>
     */
    public abstract boolean canRemove(E selected);
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the viewing of "selected". 
     *   Intended to be used primarily in view.jsf, but can be used in other JSF views.
     * </p>
     */
    public boolean canView() {
    	return canView(getSelected());
    }
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the editing of "selected". 
     *   Intended to be used primarily in edit.jsf, but can be used in other JSF views.
     * </p>
     */
    public boolean canEdit() {
    	return canEdit(getSelected());
    }
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the removing of "selected". 
     *   Intended to be used primarily in remove.jsf, but can be used in other JSF views.
     * </p>
     */
    public boolean canRemove() {
    	return canRemove(getSelected());
    }
    
}