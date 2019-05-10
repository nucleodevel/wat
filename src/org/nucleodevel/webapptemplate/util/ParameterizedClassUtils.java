package org.nucleodevel.webapptemplate.util;

import java.lang.reflect.ParameterizedType;

/**
 * <p>
 *   Implementa algumas funcionalidades úteis para classes parametrizadas.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class ParameterizedClassUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    
    /**
	 * <p>
	 *   Obtém o tipo Class do tipo-parâmetro definido em parameterizedClass e cujo índice é 
	 *   parameterIndex. 
	 * </p>
	 * @param parameterizedClass Classe que contém os tipos-parâmetro.
	 * @param parameterIndex Indíce do parâmetro cuja classe será obtida.
	 * @return O tipo Class do tipo-parâmetro definido em parameterizedClass e cujo índice é 
	 *   parameterIndex.
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