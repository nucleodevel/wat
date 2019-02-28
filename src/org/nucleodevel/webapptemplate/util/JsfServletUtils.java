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
 *   Implements some useful servlet functionality for use in managed beans.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class JsfServletUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    /**
	 * <p>
	 *   Vector that stores the operating systems of mobile devices.
	 * </p>
	 */
	private static String[] mobiles = {"android", "iphone", "ipod", "symbian"};
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * <p>
	 *   Uses the information from externalAgent and verifies that the User-Agent property contains 
	 *   one of the mobile operating systems. Returns this as true or false.
	 * </p>
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
	 *   Provides output to a stream in the context of a servlet call. Corresponds to a stream 
	 *   download as a file whose name will be this second parameter.
	 * </p>
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