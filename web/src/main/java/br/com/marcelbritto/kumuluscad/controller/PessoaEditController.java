package br.com.marcelbritto.kumuluscad.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Endereco;
import br.com.marcelbritto.kumuluscad.model.Estado;
import br.com.marcelbritto.kumuluscad.model.Pessoa;
import br.com.marcelbritto.kumuluscad.service.EstadoFacade;
import br.com.marcelbritto.kumuluscad.service.PessoaFacade;
import lombok.Data;

@ManagedBean(name = "pessoaEditController")
@ViewScoped
@Data
public class PessoaEditController extends BaseController {

	@Inject
	private transient Logger logger;

	@Inject
	private PessoaFacade facade;

	@Inject
	private EstadoFacade estadoFacade;
		
	private Pessoa pessoa;
	private Set<Endereco> selectedEnderecoList;
	private Endereco selectedEndereco;
	private Endereco enderecoEdit;
	private List<Estado> estadoList;
	private List<Cidade> cidadeList;
	private Estado selectedEstado;
	private Cidade selectedCidade;
	private boolean insertMode = false;
	
	public PessoaEditController() {
		context = FacesContext.getCurrentInstance();
		bundle = context.getApplication().getResourceBundle(context, "msgs");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("pt", "BR"));
	}
	
	//Para testes
	public PessoaEditController(FacesContext facesContext, ResourceBundle resourceBundle ) {
		context = facesContext;
		bundle = resourceBundle;
		
	}
	
	@PostConstruct
	public void init() {
		resetPessoa();
		
		// Carregar em caso de alteração
		pessoa = (Pessoa) context.getExternalContext().getFlash().get("pessoa");
		
		if (pessoa != null) {
			selectedEnderecoList = facade.listEndereco(pessoa);
							
		} else {
			resetPessoa();
			insertMode = true;
		}
			
	}
	
	
	public void resetPessoa()  {
		pessoa = new Pessoa();
		selectedEnderecoList = new HashSet<>();
		enderecoEdit = new Endereco();
		selectedEndereco = new Endereco();
		selectedCidade = new Cidade();
		selectedEstado = new Estado();
		selectedEstado.setCidades(new ArrayList<>());
	}

	// Realiza a consulta tanto pelo nome do profissional, quanto pelo o seu CPF caso esteja presente na string.
//	int pos = person.getName().indexOf("-");
//	String name = (person.getName().substring(0,pos)).trim();

//	public void findPersonByName() {
//		try {
//			String[] values = person.getName().split("-");
//			if(values.length == 2) {
//				List<Person> list = personFacade.findByCpf(values[1].trim());
//				person = (list != null && !list.isEmpty()) ? list.get(0) : null;
//			}
//			else
//				person = personFacade.getByName(values[0]);
//		} catch (Exception e) {
//			logger.error(e);
//		}
//	}



	public void insertEndereco() {
		if (enderecoEdit != null) {
			enderecoEdit.setCidade(selectedCidade.getNome());
			enderecoEdit.setEstado(selectedEstado.getInitial());
			enderecoEdit.setPessoa(pessoa);
			selectedEnderecoList.add(enderecoEdit);
			enderecoEdit = new Endereco();
		}
	}
	
	public void deleteEndereco() {
		selectedEnderecoList.remove(selectedEndereco);
	}


	public void save() {
		FacesMessage message = null;
		List<String> errorList = new ArrayList<String>();
		try {
			// Validando Campos Obrigatórios.
			facade.validateRequiredFields(pessoa, errorList);
			if (selectedEnderecoList == null || selectedEnderecoList.isEmpty()) {
				pessoa.setEnderecos(null);
			} else {
				pessoa.setEnderecos(selectedEnderecoList);
			}
			
			if (!errorList.isEmpty()) {
				String errorTitle = bundle.getString("error_validate_required_fields");
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, "");
				context.addMessage("msgsGlobal", message);
				errorList.forEach(error -> context.addMessage("msgsGlobal",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, this.bundle.getString(error), null)));

				return;
			}
			if (insertMode) {
				facade.create(pessoa);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, this.bundle.getString("msg_insert_success"), "");
				resetPessoa();
			} else {
				facade.update(pessoa);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, this.bundle.getString("msg_update_success"), "");
			}

			
			context.addMessage("msgsGlobal", message);
	
		} catch (Exception e) {
			if (pessoa.getNome() == null)  {
				if (insertMode) {
					this.handleException(e, this.bundle.getString("error_create").replace("{0}", "Pessoa"), logger);
				} else {
					this.handleException(e, this.bundle.getString("error_update").replace("{0}", "Pessoa"), logger);
				}
			} else {
				if (insertMode) {
					this.handleException(e, this.bundle.getString("error_create").replace("{0}", "Pessoa: "+ pessoa.getNome()), logger);
				} else {
					this.handleException(e, this.bundle.getString("error_update").replace("{0}", "Pessoa: "+ pessoa.getNome()), logger);
				}
			}
			
		}
	}
	
	
	
	public void checkName() {
//		List<String> result = null;
//		if (helper.getPerson() != null && helper.getPerson().getName() != null) {
//			List<Person> list = facade.listByName(helper.getPerson().getName());
//			if (list != null && !list.isEmpty()) {
//				result = new ArrayList<String>();
//				for (Person item : list) {
//					result.add(item.getName());
//				}
//			}
//		}
//		if (result != null) {
//			context.addMessage("messageName",
//					new FacesMessage(FacesMessage.SEVERITY_WARN, Messages.ERROR_NAME_EXISTS_LISTED, null));
//			for (String item : result) {
//				context.addMessage("messageName", new FacesMessage(FacesMessage.SEVERITY_WARN, item, null));
//			}
//		}
	}
	
	/**
	 * Preenche Rua, Bairro, Cidade e Estado a partir do CEP.
	 */
	public void fillCep() {
//TODO
	}
	
	/**
	 * Usado para a seleção de Estado do endereço.
	 */
	public void selectedStateListener() {
		facade.listCidades(selectedEstado);
	}
	

	
}
