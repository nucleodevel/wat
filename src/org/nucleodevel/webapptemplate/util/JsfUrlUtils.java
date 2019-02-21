package org.nucleodevel.webapptemplate.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.context.FacesContext;

/**
 * <p>
 *   Implementa diversas funcionalidades úteis de URL a serem invocadas pelos managed beans.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class JsfUrlUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
     * <p>
     *   Tenta obter o valor de um parâmetro passado via URL e trata-o como uma String. Retorna o 
     *   valor ou nulo, se tal parâmetro não foi encontrado. 
     * </p>
     * @param param Nome do parâmetro passado via URL.
     * @return String com o valor do parâmetro passado via URL.
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
     *   Tenta obter o valor de um parâmetro passado via URL e trata-o como uma Long. Retorna o 
     *   valor ou nulo, se tal parâmetro não foi encontrado. 
     * </p>
     * @param param Nome do parâmetro passado via URL.
     * @return Long com o valor do parâmetro passado via URL.
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
     *   Tenta obter o valor de um parâmetro passado via URL e trata-o como um Date. Retorna o 
     *   valor ou nulo, se tal parâmetro não foi encontrado. 
     * </p>
     * @param param Nome do parâmetro passado via URL.
     * @return Date com o valor do parâmetro passado via URL.
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