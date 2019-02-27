package org.nucleodevel.webapptemplate.util;

import java.lang.reflect.ParameterizedType;

/**
 * <p>
 *   Implements some useful functions for parameterized classes.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class ParameterizedClassUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * <p>
	 *   Gets the type Class <?> of the parameter type defined in parameterizedClass and whose 
	 *   index is parameterIndex.
	 * </p>
	 */
	public static Class<?> getParameterClassFromParameterizedClass(
		Class<?> parameterizedClass, int parameterIndex
	) {
		try {
			String parameterClassName = 
				((ParameterizedType) parameterizedClass.getGenericSuperclass())
					.getActualTypeArguments()[parameterIndex].getTypeName();
            return Class.forName(parameterClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}