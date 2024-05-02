package br.com.marcelbritto.kumuluscad.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import br.com.marcelbritto.kumuluscad.model.Pessoa;
import br.com.marcelbritto.kumuluscad.service.PessoaFacade;
import lombok.Data;

@ManagedBean(name = "pessoaSearchController")
@ViewScoped
@Data
public class PessoaSearchController extends BaseController {
	
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private PessoaFacade pessoaFacade;
	
	private String nomePessoa;
	
	private List<Pessoa> pessoas;
	
	private Pessoa pessoaSelecionada;
	
	public PessoaSearchController() {
		context = FacesContext.getCurrentInstance();
		bundle = context.getApplication().getResourceBundle(context, "msgs");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("pt", "BR"));
	}
	
	@PostConstruct
	public void init() {

		nomePessoa = "";
		pessoas = new ArrayList<Pessoa>();
		pessoaSelecionada = null;
		
	}
	
	/**
	 * Realiza a consulta de aso por funcionário.
	 */
	public String find() {
		FacesMessage message = null;
		try {
			
			if (StringUtils.isNotBlank(nomePessoa)) {
				pessoas = pessoaFacade.listByNome(nomePessoa);
				
        	} else {
        		pessoas = pessoaFacade.findAll();
        	}
			
		} catch (Exception e) {
			facesContext.addMessage("messages", new FacesMessage(bundle.getString("fatal_contact_support"),
					bundle.getString("fatal_contact_support")));
			logger.error(e.getMessage());
		}

		return null;
	}
	
	public void delete() {
		FacesMessage message = null;
		try {
			pessoaFacade.delete(pessoaSelecionada);
			pessoas.remove(pessoaSelecionada);
		} catch (Exception e) {
			this.handleException(e, this.bundle.getString("error_delete").replace("{0}", "Pessoa: " + pessoaSelecionada.getNome()), logger);
		}
	}
	
	/** 
	 * Redireciona para a página de alteração. 
	 * 
	 * @return outcome para página de alteração. 
	 */ 
	public String edit() { 
		facesContext.getExternalContext().getFlash().put("pessoa", pessoaSelecionada); 
		return "pessoa-edit"; 
	} 
	
		
	@Override
	public void handleException (Exception e, String titleMessage, Logger logger) {
		super.handleException(e, titleMessage, logger);
		PrimeFaces.current().ajax().update(":frmSearchPessoa:msgsGlobal");
		PrimeFaces.current().executeScript("document.getElementById('topDiv').scrollIntoView()");
	}
	
}
