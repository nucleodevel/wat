package org.nucleodevel.webapptemplate.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * <p>
 *   Implements methods that help construct JSF variables read from resources files. These files 
 *   are composed of lines with the following format: Key = Value. The variables are referenced by 
 *   their Key and return their Value. This strategy is widely used to provide the programmer with 
 *   a means to create their variables, especially when it involves the internationalization issue. 
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class JsfMessageUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    

    /**
     * <p>
     *   This method reads the contents of the message provided by the exception or, if this 
     *   message does not exist, of the message whose key is passed by parameter and is located in 
     *   the default file whose path is referenced by RESOURCE_GENERIC_MSG. It then adds this 
     *   message to the JSF message flow as an error message.
     * </p>
     */
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0)
            addErrorMessage(msg);
        else
            addErrorMessage(defaultMsg);
    }

    /**
     * <p>
	 *   This method reads the contents of the message whose key is passed by parameter and is 
	 *   located in the default file whose path is referenced by RESOURCE_GENERIC_MSG. It then adds 
	 *   this message to the JSF message flow as an error message.  
	 * </p>
     */
    public static void addErrorMessage(String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_ERROR, 
        	VariableUtils.getVariableFromResource("generic.error"), 
        	VariableUtils.getVariableFromResource(key)
        );
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   This method reads the contents of the message whose key is passed in the second parameter 
	 *   and is located in the file whose path is passed in the first parameter. It then adds this 
	 *   message to the JSF message flow as an error message.
	 * </p>
     */
    public static void addErrorMessage(String resource, String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_ERROR, 
        	VariableUtils.getVariableFromResource("generic.error"), 
        	VariableUtils.getVariableFromResource(resource, key)
        );
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   This method reads the contents of the message whose key is passed by parameter and is 
	 *   located in the default file whose path is referenced by RESOURCE_GENERIC_MSG. It then adds 
	 *   this message to the JSF message flow as a success message.
	 * </p>
     * @param key Chave da mensagem a ser retornada.
     */
    public static void addSuccessMessage(String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_INFO, 
        	VariableUtils.getVariableFromResource("generic.success"), 
        	VariableUtils.getVariableFromResource(key)
        );
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   This method reads the contents of the message whose key is passed in the second parameter 
	 *   and is located in the file whose path is passed in the first parameter. It then adds this 
	 *   message to the JSF message flow as a success message.
	 * </p>
     */
    public static void addSuccessMessage(String resource, String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_INFO, 
        	VariableUtils.getVariableFromResource("generic.success"), 
        	VariableUtils.getVariableFromResource(resource, key)
        );
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
	
}