package br.com.marcelbritto.kumuluscad.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;

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
	private FacesContext context;

	@Inject
	private PessoaFacade facade;

	@Inject
	private EstadoFacade estadoFacade;
		
	private Pessoa pessoa;
	private List<Endereco> selectedEnderecoList;
	private Endereco selectedEndereco;
	private Endereco enderecoEdit;
	private List<Estado> estadoList;
	private List<Cidade> cidadeList;
	private Estado selectedEstado;
	private Cidade selectedCidade;
	private boolean insertMode = false;
	
	@PostConstruct
	public void init() {
		resetPessoa();
		
		// Carregar em caso de alteração
		pessoa = (Pessoa) context.getExternalContext().getFlash().get("pessoa");
		
		if (pessoa != null) {
			Hibernate.initialize(pessoa.getEnderecos());
			insertMode = true;				
		} else {
			resetPessoa();
		}
			
	}
	
	
	public void resetPessoa()  {
		pessoa = new Pessoa();
		selectedEnderecoList = new ArrayList<>();
		enderecoEdit = new Endereco();
		selectedEndereco = new Endereco();
		selectedCidade = new Cidade();
		selectedEstado = new Estado();
		selectedEstado.setCidades(new ArrayList<>());
		System.out.println("##################### RESET PESSOA");
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
//		try {
//			if (selectedCertified != null) {
//				if (aso.getAsoCertifieds() != null) {
//					if (!aso.getAsoCertifieds().contains(selectedCertified) && selectedCertified.getName() != null) {
//						aso.getAsoCertifieds().add(selectedCertified);
//						selectedCertified = new Certified();
//					}
//				} else if (!selectedCertified.getName().isBlank()) {
//					aso.setAsoCertifieds(new ArrayList<>());
//					aso.getAsoCertifieds().add(selectedCertified);
//					selectedCertified = new Certified();
//				}
//			}
//			
//		} catch (Exception e) {
//			logger.error(e);
//		}
	}
	
	public void deleteEndereco() {
//		FacesMessage message = null;
//		try {
//			pessoaFacade.delete(pessoaSelecionada);
//			pessoas.remove(pessoaSelecionada);
//		} catch (Exception e) {
//			this.handleException(e, this.bundle.getString("error_delete").replace("{0}", "Pessoa: " + pessoaSelecionada.getNome()), logger);
//		}
	}


	public void save() {
		FacesMessage message = null;
		List<String> errorList = new ArrayList<String>();
		try {
			// Validando Campos Obrigatórios.
			validateRequiredFields(errorList);
			
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
			} else {
				facade.update(pessoa);
			}

			
			context.addMessage("msgsGlobal", message);
	
		} catch (Exception e) {
			if (pessoa.getNome() == null)  {
				this.handleException(e, this.bundle.getString("erroInclusao").replace("{0}", "Pessoa"), logger);
			} else {
				this.handleException(e, this.bundle.getString("erroInclusao").replace("{0}", pessoa.getNome()), logger);
			}
			
		}
	}
	
	private void validateRequiredFields(List<String> errorList) {
		if (StringUtils.isBlank(pessoa.getNome())) {
			errorList.add("error_fill_name");
		}
		
		if (StringUtils.isBlank(pessoa.getCpf())) {
			errorList.add("error_fill_cpf");
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
//		final String cep = helper.getPerson().getCep();
//		Map<String, String> cepMap = ClientCorreioWS.getMapFromCep(cep);
//
//		if (cepMap != null && !cepMap.isEmpty()) {
//			Map<String, City> cityMap = new HashMap<>();
//			Map<String, State> stateMap = getDomainProducer().stateList().stream()
//					.collect(Collectors.toMap(State::getInitial, state -> state));
//
//			helper.getPerson().setAddress(cepMap.get(ClientCorreioWS.RUA));
//			helper.getPerson().setNeighborhood(cepMap.get(ClientCorreioWS.BAIRRO));
//
//			helper.getPerson().setStateAddress(stateMap.get(cepMap.get(ClientCorreioWS.UF)));
//			helper.getPerson().setCountryAddress(helper.getPerson().getStateAddress().getCountry());
//			// ^^ sempre será Brasil, mas optei por fazer assim pra sempre pegar
//			// o objeto do pais corretamente
//
//			helper.getPerson().getStateAddress().getCityList()
//					.forEach(city -> cityMap.put(city.getName().trim(), city));
//			// as cidades estão com espaços em branco que atrapalham a comparação ^^
//
//			helper.getPerson().setCityAddress(cityMap.get(cepMap.get(ClientCorreioWS.CIDADE)));
//
//			if (!cep.contains("-"))
//				helper.getPerson().setCep(cep.substring(0, 5) + "-" + cep.substring(5, 8));
//
//			helper.setSelectedStateAddress(helper.getPerson().getStateAddress().getName());
//			helper.setSelectedCityAddress(helper.getPerson().getCityAddress().getName());
//		}
	}
	
	/**
	 * Usado para a seleção de Estado do endereço.
	 */
	public void selectedStateAddressListener() {
		facade.listCidades(selectedEstado);
	}

	/**
	 * Usado para a seleção de Cidade do endereço.
	 */
	public void selectedCityAddressListener() {

//		if (helper.getPerson().getStateAddress() != null) {
//			for (City city : helper.getPerson().getStateAddress().getCityList()) {
//				if (city.getName().equalsIgnoreCase(helper.getSelectedCityAddress())) {
//					helper.getPerson().setCityAddress(city);
//					break;
//				}
//			}
//
//			if (helper.getPerson().getCityAddress() == null) {
//				City city = new City();
//				city.setName(helper.getSelectedStateAddress());
//				helper.getPerson().setCityAddress(city);
//			}
//		}

	}
}
