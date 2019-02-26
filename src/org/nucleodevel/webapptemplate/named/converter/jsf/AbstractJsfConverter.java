package org.nucleodevel.webapptemplate.named.converter.jsf;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Abstract class used by JSF to obtain an instance of E of a string that contains the identifier 
 *   of that instance. This string is usually provided by JSF components such as selects, radio 
 *   buttons, and checkboxes, and the convert requests the corresponding instance to DAO.
 * <p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 * @param <DAO> DAO class that provides E entities.
 */
@SuppressWarnings("rawtypes")
@Named
public abstract class AbstractJsfConverter<E extends AbstractEntity<?>, DAO extends AbstractDao<?>> 
	implements Converter {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Considere que a classe que estende AbstractJsfConverter seja C,todo C deve estar associado 
     *   a uma subclasse de AbstractDao DAO e ambas, C e DAO, tenham uma classe entidade E como 
     *   tipo parametrizado. DAO será responsável por fornecer instâncias do tipo E que C 
     *   necessitar.
     * </p>
     */
    @Inject
    private DAO dao;
    
	/**
     * <p>
     *   Atributo que armazena a classe assumida por E, que é a entidade alvo do converter. 
     *   Geralmente usado para se obter nome desta classe.
     * </p>
     */
    private Class<E> entityClass;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
    

    /**
     * <p>
     *   Obtém tipo class do tipo E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils.
    				getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(
	 *     javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String
	 * )
	 */
	@Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
    	if (value == null || value.length() == 0 || getIdFromString(value) == null)
    		return null;
   	    return dao.selectOne(getIdFromString(value));
    }

    /* (non-Javadoc)
     * @see javax.faces.convert.Converter#getAsString(
     *     javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object
     * )
     */
    @SuppressWarnings("unchecked")
	@Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null)
            return null;
       
        E entity = (E) object;
        String entityClassName = getEntityClass().getName();
       
        if (object.getClass().getName().compareTo(entityClassName) == 0) {
        	StringBuilder sb = new StringBuilder();
        	sb.append(entity.getEntityId());
        	return sb.toString();
        }
        else {
            Logger.getLogger(this.getClass().getName()).log(
            	Level.SEVERE, "object {0} is of type {1}; expected type: {2}", 
            	new Object[] {object, object.getClass().getName(), entityClassName}
            );
            return null;
        }
    }

    /**
     * <p>
     *   Converte o valor String de um ID para o valor do tipo correto do ID.
     * </p>
     * @param idString Valor string do ID.
     * @return Valor do tipo correto do ID.
     */
    @SuppressWarnings("unchecked")
	protected Object getIdFromString(String idString) {
    	E newEntity = (E) dao.getNewEntityInstance();
		String entityIdClass = newEntity.getEntityIdClass().getSimpleName();
		
        try {
        	if (entityIdClass.equals("Long"))
    			return Long.parseLong(idString);
    		else if (entityIdClass.equals("Integer"))
    			return Integer.parseInt(idString);
    		else if (entityIdClass.equals("Short"))
    			return Short.parseShort(idString);
    		else
    			return idString;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}