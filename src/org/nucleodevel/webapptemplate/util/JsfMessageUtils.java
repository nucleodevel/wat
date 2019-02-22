package org.nucleodevel.webapptemplate.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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
public class JsfMessageUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Métodos
	 * --------------------------------------------------------------------------------------------
	 */
    

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
        	VariableUtils.getVariableFromResource("generic.error"), 
        	VariableUtils.getVariableFromResource(key)
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
        	VariableUtils.getVariableFromResource("generic.error"), 
        	VariableUtils.getVariableFromResource(resource, key)
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
        	VariableUtils.getVariableFromResource("generic.success"), 
        	VariableUtils.getVariableFromResource(key)
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
        	VariableUtils.getVariableFromResource("generic.success"), 
        	VariableUtils.getVariableFromResource(resource, key)
        );
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
	
}