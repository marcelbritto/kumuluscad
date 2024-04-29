
package br.com.marcelbritto.kumuluscad.controller;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.util.DomainProducers;


/**
 * Classe base para os Controllers da aplicação. 
 * 
 * @author marcelbritto
 */

@ManagedBean
public abstract class BaseController {
	
	protected ResourceBundle bundle;
	
	@Inject
	protected transient Logger logger;
	
	
//	@ManagedProperty(value = "#{domainProducer}")
//	private DomainProducers domainProducer;
	
	
	public BaseController() {
		FacesContext context = FacesContext.getCurrentInstance();
		bundle = context.getApplication().getResourceBundle(context, "msgs");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("pt", "BR"));
	}
	
	
//	public DomainProducers getDomainProducer() {
//		return domainProducer;
//	}
//
//	public void setDomainProducer(DomainProducers domainProducer) {
//		this.domainProducer = domainProducer;
//	}

	public void addMessage(String summary) {
		addMessage(summary, FacesMessage.SEVERITY_INFO);
	}
	
	public void addMessage(String summary, FacesMessage.Severity severityInfo) {
		FacesMessage message = new FacesMessage(severityInfo, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	/**
	 * Adiciona mensagem informativa à tela.
	 * 
	 * @param summary
	 */
	public void addInfoMessage(String summary) {
		addMessage(null, FacesMessage.SEVERITY_INFO, summary);
	}

	/**
	 * Adiciona mensagem de erro à tela.
	 * 
	 * @param summary
	 */
	public void addErrorMessage(String summary) {
		addMessage(null, FacesMessage.SEVERITY_ERROR, summary);
	}

	/**
	 * Adiciona mensagem informativa à tela para o determinado ID de componente
	 * 
	 * @param clientId
	 * @param summary
	 */
	public void addInfoMessage(String clientId, String summary) {
		addMessage(summary, FacesMessage.SEVERITY_INFO, clientId);
	}

	/**
	 * Adiciona mensagem de aviso à tela para o determinado ID de componente
	 * 
	 * @param clientId
	 * @param summary
	 */
	public void addWarnMessage(String clientId, String summary) {
		addMessage(summary, FacesMessage.SEVERITY_WARN, clientId);
	}

	/**
	 * Adiciona mensagem de erro à tela para o determinado ID de componente
	 * 
	 * @param clientId
	 * @param summary
	 */
	public void addErrorMessage(String clientId, String summary) {
		addMessage(summary, FacesMessage.SEVERITY_ERROR, clientId);
	}

	/**
	 * Adiciona mensagem de erro fatal à tela para o determinado ID de componente
	 * 
	 * @param clientId
	 * @param summary
	 */
	public void addFatalMessage(String clientId, String summary) {
		addMessage(summary, FacesMessage.SEVERITY_FATAL, clientId);
	}

	/**
	 * Adiciona mensagem à tela para o determinado ID de componente.
	 *
	 * @param summary
	 * @param severityInfo
	 * @param clientId
	 */
	private void addMessage(String summary, FacesMessage.Severity severityInfo, String clientId) {
		FacesMessage message = new FacesMessage(severityInfo, summary, null);
		FacesContext.getCurrentInstance().addMessage(clientId, message);
		PrimeFaces.current().executeScript("document.getElementById('messagesDiv').scrollIntoView()");
	}
	
	/**
	 * Adiciona mensagem à tela com título e detalhe.
	 * 
	 * @param summary
	 * @param detail
	 * @param severityInfo
	 */
	public void addMessage(String summary, String detail, FacesMessage.Severity severityInfo) {
		FacesMessage message = new FacesMessage(severityInfo, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	/**
	 * Trata os erros. Se for um BusinessException, significa que existe uma mensagem amigável para o usuário
	 * e ela será exibida. Caso contrário, apenas indica, de forma superficial, onde o erro ocorreu.
	 * 
	 * @param e - exceção
	 * @param titleMessage - título da mensagem de erro (geralmente indica onde o erro ocorreu.
	 * @param logger - log4j
	 */
	public void handleException (Exception e, String titleMessage, Logger logger) {
		FacesMessage message = null;
		
		logger.error(e.getLocalizedMessage(), e);
		if (e instanceof BusinessException) {
    		
        	message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titleMessage, e.getMessage());
        	FacesContext.getCurrentInstance().addMessage("msgsGlobal", message);
        
    	} else {

    		message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titleMessage, "");
        	FacesContext.getCurrentInstance().addMessage("msgsGlobal", message);
            
    	}
	}
}
