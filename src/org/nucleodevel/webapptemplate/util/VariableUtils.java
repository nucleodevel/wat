package org.nucleodevel.webapptemplate.util;

import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>
 *   Implements methods that help construct variables read from resource files or the Tomcat 
 *   context. These files are composed of lines with the following construct: Key = Value. A 
 *   variable is referenced by its key and is returned its value. This strategy is widely used to 
 *   provide the programmer with a means of creating their variables, especially in the matter of 
 *   environment configuration.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class VariableUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    /**
	 * Default path of the custom variable file used by webapptemplate. As an example we have the 
	 * default error and success variables, improper access, etc.
	 */
	private static final String RESOURCE_GENERIC_MSG = "/resources/generic-messages";
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    /**
     * <p>
     *   Attempts to get the value of a variable defined in the server environment.
     * </p>
     */
    public static String getVariableFromEnvironment(String key) {
    	InitialContext initialContext = null;
        try {
			initialContext = new javax.naming.InitialContext();
			return (String) initialContext.lookup("java:comp/env/" + key);
	    } catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		return null;
    }
    
    
    /**
	 * <p>
	 *   This method supplies the contents of the variable whose key is passed by parameter and is 
	 *   located in the default file whose path is referenced by RESOURCE_GENERIC_MSG.
	 * </p>
	 */
	public static String getVariableFromResource(String key) {
		return ResourceBundle.getBundle(RESOURCE_GENERIC_MSG).getString(key);
	}
	
	/**
	 * <p>
	 *   This method provides the contents of the variable whose key is passed as the second 
	 *   parameter and is located in the file whose path is passed in the first parameter.
	 * </p>
	 */
	public static String getVariableFromResource(String resource, String key) {
		return ResourceBundle.getBundle(resource).getString(key);
	}
	
}