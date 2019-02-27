package org.nucleodevel.webapptemplate.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.context.FacesContext;

/**
 * <p>
 *   Implements several useful URL functionalities to be invoked by managed beans.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class JsfUrlUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
     * <p>
     *   Tries to get the value of a parameter passed via URL and treats it as a String. Returns 
     *   the value or null if this parameter was not found.
     * </p>
     */
    public static String getUrlStringParam(String param) {
    	String value = 
    		"" 
    		+ FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
    			.get(param);
		if (!(value.equals("") || value.equals("null")))
			return value;
		return null;
    } 
    
    /**
     * <p>
     *   Tries to get the value of a parameter passed via URL and treats it as a Long. Returns 
     *   the value or null if this parameter was not found.
     * </p>
     */
    public static Long getUrlLongParam(String param) {
    	try {
	    	return Long.parseLong(
	    		FacesContext.getCurrentInstance().getExternalContext()
	    			.getRequestParameterMap().get(param)
	    	);
    	} catch (NumberFormatException e) {
    		return null;
    	}
    } 
    
    /**
     * <p>
     *   Tries to get the value of a parameter passed via URL and treats it as a Date. Returns 
     *   the value or null if this parameter was not found.
     * </p>
     */
    public static Date getUrlDateParam(String param) {
    	String strValue = getUrlStringParam(param); 
		if (strValue != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return format.parse(strValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
	
}