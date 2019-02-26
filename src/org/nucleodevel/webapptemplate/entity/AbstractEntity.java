package org.nucleodevel.webapptemplate.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Abstract class that implements common methods for entities that belong to a datasource and are 
 *   handled by DAOs.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <TID> Type of entity's ID. 
 */
public abstract class AbstractEntity<TID> implements Comparable<AbstractEntity<TID>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Attribute that stores the class adopted by TID, which is the ID of the entity.
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
     *   Returns the class<?> of TID via ParameterizedClassUtils
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
	 *   A subclass must implement a method that returns the value of its ID, since only it knows 
	 *   which attribute is the ID.
	 * </p>
	 */
    @XmlTransient
	public abstract TID getEntityId();
	
	/**
	 * <p>
	 *   A subclass must implement a method that sets the value of its ID, since only it knows 
	 *   which attribute is the ID.
	 * </p>
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
     *   A subclass must implement a method that returns a string that displays the entity's basic 
     *   information in a friendly format.
     * </p>
     */
    protected abstract String getViewString();
	
	/**
     * <p>
     *   A subclass must implement a method that returns a string that displays the entity's basic 
     *   information for comparison with another.
     * </p>
     */
	protected String getComparableString() {
		return getViewString().toLowerCase();
	}

}