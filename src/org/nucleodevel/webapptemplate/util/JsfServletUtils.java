package org.nucleodevel.webapptemplate.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * <p>
 *   Implementa algumas funcionalidades úteis sobre servlets para uso nos managed beans.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class JsfServletUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    /**
	 * <p>
	 *   Vetor que armazena os sistemas operacionais de dispositivos móveis.
	 * </p>
	 */
	private static String[] mobiles = {"android", "iphone", "ipod", "symbian"};
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * <p>
	 *   Utiliza a informação de externalAgent e verifica se a propriedade User-Agent contém um dos 
	 *   sistemas operacionais de dispositivos móveis. Retorna isto como verdadeiro ou falso, em 
	 *   caso contrário. 
	 * </p>
	 * @return Booleano que indica se o dispositivo que está utilizando o sistema é considerado 
	 *   móvel ou não.
	 */
	public static boolean isMobile() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    String userAgent = externalContext.getRequestHeaderMap().get("User-Agent").toUpperCase();

		for (String mobile: mobiles)
    		if (userAgent.contains(mobile.toUpperCase()))
    			return true;
    	
    	return false;
	}
	
	/**
	 * <p>
	 *   Dá saída a um stream no contexto da chamada de um servlet. Corresponde a um download do 
	 *   stream como um arquivo. 
	 * </p>
	 * @param stream Stream a ser dado como saída.
	 * @param name Nome de arquivo a ser fornecido ao stream.
	 * @throws IOException
	 */
	public static void showInputStream(ByteArrayInputStream stream, String name) 
		throws IOException {
		
		HttpServletResponse httpServletResponse = 
			(HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=\"" + name);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        
        IOUtils.copy(stream, servletOutputStream);
        
        stream.reset();
        
        FacesContext.getCurrentInstance().responseComplete();
	}
	
}