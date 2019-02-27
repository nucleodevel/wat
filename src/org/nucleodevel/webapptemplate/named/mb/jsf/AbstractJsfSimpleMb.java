package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;

import org.nucleodevel.webapptemplate.named.mb.AbstractMb;
import org.nucleodevel.webapptemplate.session.dao.jsf.AbstractSessionJsfDao;

/**
 * <p>
 *   Abstract subclass of AbstractMb that implements the default behavior of a JSF managed bean.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <SDAO> DAO class that maps the current HTTP session of the application that owns this 
 *   managed bean.
 */
public abstract class AbstractJsfSimpleMb<SDAO extends AbstractSessionJsfDao>
	extends AbstractMb<SDAO> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
    
}