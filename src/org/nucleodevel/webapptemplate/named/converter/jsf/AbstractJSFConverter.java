package org.nucleodevel.webapptemplate.named.converter.jsf;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import org.nucleodevel.webapptemplate.dao.AbstractDAO;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**<p>
 *   Classe utilizada pelo JSF para recuperar uma instância do tipo E a partir da string que contém 
 *   o valor do identificador desta instância. A classe DAO, subclasse de AbstractDAO, é a classe 
 *   que obtém entidades do tipo E do datasource.
 * </p>
 * <p>  
 *   Usado principalmente em itens de formulário como selects, radio buttons e checkboxes do JSF, 
 *   que possibilitam ao usuário escolher uma entidade como valor e não simplesmente um ID de uma 
 *   entidade.
 * <p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 * @param <DAO> Classe DAO que fornece as entidades E para este converter.
 */
@SuppressWarnings("rawtypes")
@Named
public abstract class AbstractJSFConverter<E extends AbstractEntity<?>, DAO extends AbstractDAO<?>> 
	implements Converter {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Considere que a classe que estende AbstractJSFConverter seja C,todo C deve estar associado a 
     *   uma subclasse de AbstractDAO DAO e ambas, C e DAO, tenham uma classe entidade E como tipo 
     *   parametrizado. DAO será responsável por fornecer instâncias do tipo E que C necessitar.
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