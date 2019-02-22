package org.nucleodevel.webapptemplate.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class JsfVariableUtils {
	
	
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

    /**
     * <p>
     *   Este método lê o conteúdo da mensagem fornecida pela exceção ou, se esta mengagem não 
     *   existir, da mensagem cuja chave é passada por parâmetro e está localizada no arquivo 
     *   padrão cujo caminho é referenciado por RESOURCE_GENERIC_MSG. Depois, adiciona esta 
     *   mensagem ao fluxo de mensagens JSF como uma mensagem de erro. 
     * </p>
     * @param ex Exceção que motiva a mensagem de erro.
     * @param defaultMsg Mensagem usada no caso da exceção não fornecer mensagem alguma.
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
	 *   Este método lê o conteúdo da mensagem cuja chave é passada por parâmetro e está localizada 
	 *   no arquivo padrão cujo caminho é referenciado por RESOURCE_GENERIC_MSG. Depois, adiciona 
	 *   esta mensagem ao fluxo de mensagens JSF como uma mensagem de erro.  
	 * </p>
     * @param key Chave da mensagem a ser retornada.
     */
    public static void addErrorMessage(String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_ERROR, 
        	getVariableFromResource("generic.error"), 
        	getVariableFromResource(key)
        );
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   Este método lê o conteúdo da mensagem cuja chave é passada no 2° parâmetro e está 
	 *   localizada no arquivo cujo caminho é passado no 1° parâmetro. Depois, adiciona esta 
	 *   mensagem ao fluxo de mensagens JSF como uma mensagem de erro.  
	 * </p>
     * @param resource Caminho absoluto do arquivo onde a mensagem está armazenada.
	 * @param key Chave da mensagem a ser retornada.
     */
    public static void addErrorMessage(String resource, String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_ERROR, 
        	getVariableFromResource("generic.error"), 
        	getVariableFromResource(resource, key)
        );
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   Este método lê o conteúdo da mensagem cuja chave é passada por parâmetro e está localizada 
	 *   no arquivo padrão cujo caminho é referenciado por RESOURCE_GENERIC_MSG. Depois, adiciona 
	 *   esta mensagem ao fluxo de mensagens JSF como uma mensagem de sucesso.  
	 * </p>
     * @param key Chave da mensagem a ser retornada.
     */
    public static void addSuccessMessage(String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_INFO, 
        	getVariableFromResource("generic.success"), 
        	getVariableFromResource(key)
        );
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * <p>
	 *   Este método lê o conteúdo da mensagem cuja chave é passada no 2° parâmetro e está 
	 *   localizada no arquivo cujo caminho é passado no 1° parâmetro. Depois, adiciona esta 
	 *   mensagem ao fluxo de mensagens JSF como uma mensagem de erro.  
	 * </p>
     * @param resource Caminho absoluto do arquivo onde a mensagem está armazenada.
	 * @param key Chave da mensagem a ser retornada.
     */
    public static void addSuccessMessage(String resource, String key) {
        FacesMessage facesMsg = new FacesMessage(
        	FacesMessage.SEVERITY_INFO, 
        	getVariableFromResource("generic.success"), 
        	getVariableFromResource(resource, key)
        );
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
	
}