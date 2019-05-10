package org.nucleodevel.webapptemplate.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Classe abstrata que implementa métodos comuns às entidades que pertencem a um datasource e são 
 *   manipuladas pelos DAOs.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <TID> Tipo do ID da entidade.
 */
public abstract class AbstractEntity<TID> implements Comparable<AbstractEntity<TID>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Atributo que armazena a classe assumida por TID, que é o tipo do ID da entidade. Geralmente 
     *   usado para se obter nome desta classe.
     * </p>
     */
    private Class<TID> entityIdClass;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */

    
	/**
     * <p>
     *   Obtém tipo class do tipo TID via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	public Class<TID> getEntityIdClass() {
    	if (entityIdClass == null)
    		entityIdClass = 
    			(Class<TID>) ParameterizedClassUtils
    				.getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityIdClass;
    }

    /**
	 * <p>
	 *   É delegado às subclasses a definição do atributo ID, pois somente essas classes conhecem 
	 *   qual de seus atributos são considerados IDs. Por isso, o getter deste ID precisa ser 
	 *   definido nestas subclasses.
	 * </p>
	 * @return Atual valor do ID da entidade.
	 */
    @XmlTransient
	public abstract TID getEntityId();
	
	/**
	 * <p>
	 *   É delegado às subclasses a definição do atributo ID, pois somente essas classes conhecem 
	 *   qual de seus atributos são considerados IDs. Por isso, o setter deste ID precisa ser 
	 *   definido nestas subclasses.
	 * </p>
	 * @param id Novo valor do ID da entidade.
	 */
	public abstract void setEntityId(TID id);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos de entidade
	 * --------------------------------------------------------------------------------------------
	 */
    

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (getEntityId() != null ? getEntityId().hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
    	String thisClassName = this.getClass().getName();
        
        if (object.getClass().getName().compareTo(thisClassName) != 0)
            return false;
        
        @SuppressWarnings("unchecked")
		AbstractEntity<TID> other = (AbstractEntity<TID>) object;
        if (
        	(this.getEntityId() == null && other.getEntityId() != null) 
        	|| (this.getEntityId() != null && !this.getEntityId().equals(other.getEntityId()))
        )
            return false;
        
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getViewString();
    }

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AbstractEntity<TID> another) {
		return this.getComparableString().compareTo(another.getComparableString());
	}
    
    /**
     * <p>
     *   Retorna uma string que mostra os dados principais da entidade em uma forma mais adequada à 
     *   visualização. Como cada subclasse conhece quais são seus atributos relevantes, elas devem 
     *   implementar este método.
     * </p>
     * @return String de exibição da entidade.
     */
    protected abstract String getViewString();
	
	/**
     * <p>
     *   Retorna uma string que mostras os dados principais da entidade em uma forma mais adequada à 
     *   comparação. Como cada subclasse conhece quais são seus atributos relevantes, elas devem 
     *   implementar este método.
     * </p>
     * @return String de comparação da entidade.
     */
	protected String getComparableString() {
		return getViewString().toLowerCase();
	}

}