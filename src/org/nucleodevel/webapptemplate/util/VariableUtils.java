package org.nucleodevel.webapptemplate.util;

import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>
 *   Implementa métodos que auxiliam a construção de variáveis JSF lidas dos arquivos resources. 
 *   Estes arquivos são compostos de linhas com a seguinte construção: Key=Value. As variáveis são 
 *   referenciadas por sua chave Key e devolvem o valor Value. Esta estratégia é muito usada para 
 *   fornecer ao programador um meio de criar suas variáveis, principalmente quando envolve a 
 *   questão de internacionalização.  
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class VariableUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */

	
    /**
	 * Caminho padrão do arquivo de variáveis customizadas usadas por webapptemplate. Como exemplo 
	 * temos as variáveis-padrão de erro e sucesso, acesso indevido, etc.
	 */
	private static final String RESOURCE_GENERIC_MSG = "/resources/generic-messages";
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    
    /**
     * <p>
     *   Tenta obter o valor de uma variável definida no ambiente do servidor. 
     * </p>
     * @param key Nome da variável de ambiente.
     * @return Valor da variável de ambiente cujo nome foi passado por parâmetro.
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
	 *   Este método fornece o conteúdo da variável cuja chave é passada por parâmetro e está 
	 *   localizada no arquivo padrão cujo caminho é referenciado por RESOURCE_GENERIC_MSG.
	 * </p>
	 * @param key Chave da variável a ser retornada.
	 * @return Valor (conteúdo) da variável a ser retornada.
	 */
	public static String getVariableFromResource(String key) {
		return ResourceBundle.getBundle(RESOURCE_GENERIC_MSG).getString(key);
	}
	
	/**
	 * <p>
	 *   Este método fornece o conteúdo da variável cuja chave é passada como 2° parâmetro e está 
	 *   localizada no arquivo cujo caminho é passado no 1° parâmetro.
	 * </p>
	 * @param resource Caminho absoluto do arquivo onde a variável está armazenada.
	 * @param key Chave da variável a ser retornada.
	 * @return Valor (conteúdo) da variável a ser retornada.
	 */
	public static String getVariableFromResource(String resource, String key) {
		return ResourceBundle.getBundle(resource).getString(key);
	}
	
}