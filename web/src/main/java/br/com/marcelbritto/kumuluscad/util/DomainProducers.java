package br.com.marcelbritto.kumuluscad.util;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.Logger;

import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Estado;
import br.com.marcelbritto.kumuluscad.model.Sexo;
import br.com.marcelbritto.kumuluscad.service.CidadeFacade;
import br.com.marcelbritto.kumuluscad.service.EstadoFacade;

/**
 * Classe produtora de domínios para a camada de apresentação.
 *
 * @author Marcel Britto
 */
@ManagedBean(name = "domainProducer")
@SessionScoped
public class DomainProducers implements Serializable {

	/** Constante de Serialização. */
	private static final long serialVersionUID = 3195518563196542411L;

	/** Instância do Logger utilizado para gravar registros de log da aplicação. */
	@Inject
	private transient Logger logger;

	@Inject
	private EstadoFacade estadoFacade;
	
	@Inject
	private CidadeFacade cidadeFacade;
	
	/** Atributo resposavel por armazenar formato de data **/
	private static final Format DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

	/**
	 * Producer para obter a lista de estados.
	 *
	 * @return lista de estados.
	 */
	@SuppressWarnings("finally")
	public List<Estado> stateList() {
		List<Estado> list = new ArrayList<>();
		try {
			list = estadoFacade.listStates();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			return list;
		}
	}

	/**
	 * Producer para obter a lista de cidades.
	 *
	 * @return lista de cidades.
	 */
	@SuppressWarnings("finally")
	public List<Cidade> cityList() {
		List<Cidade> list = new ArrayList<Cidade>();
		try {
			list = cidadeFacade.listCities();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			return list;
		}
	}

	/**
	 * Producer para obter a lista de Genero.
	 *
	 * @return lista de Genero.
	 */
	@SuppressWarnings("finally")
	public Sexo[] sexList() {
		Sexo[] list = {};
		try {
			list = Sexo.values();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			return list;
		}
	}

	
	@Produces
	@Named("datePickerMaxDate")
	public String produceDatePickerMaxDate() {
		return DD_MM_YYYY.format(new Date());
	}

	

	
}
