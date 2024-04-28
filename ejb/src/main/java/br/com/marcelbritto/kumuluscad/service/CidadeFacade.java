package br.com.marcelbritto.kumuluscad.service;

import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import br.com.marcelbritto.kumuluscad.dao.CidadeDAO;
import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Estado;

/**
 *
 * @author marcelbritto
 *
 */

@Stateless
public class CidadeFacade{

	@Inject
	private transient Logger logger;

	@Inject
	private CidadeDAO cidadeDAO;

	private static final String NOME_ARQUIVO_CONSTANTES = "META-INF/ApplicationResources";

	private static ResourceBundle bundle = ResourceBundle.getBundle(NOME_ARQUIVO_CONSTANTES);

	/**
	 * Retorna a lista de cidades.
	 *
	 * @return List<Cidade> {@link Cidade}
	 */
	public List<Cidade> listCities() throws BusinessException {
		try {

			return cidadeDAO.getList();

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new BusinessException(bundle.getString("error_load_cities"), e);
		}
	}

	/**
	 * Retorna a lista de cidades.
	 *
	 * @return List<Cidade> {@link Cidade}
	 */
	public List<Cidade> listCities(Estado estado) throws BusinessException {
		try {

			String[] parameters = { "estado" };
			Integer[] values = { estado.getId() };
			return cidadeDAO.getObjects("Cidade.findByEstado", parameters, values);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new BusinessException(bundle.getString("error_load_cities"), e);
		}
	}
	
	/**
	 * Retorna a cidade.
	 *
	 * @return List<Cidade> {@link Cidade}
	 */
	public Cidade getCidade(Integer id) throws BusinessException {
		try {
			return cidadeDAO.find(id);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new BusinessException(bundle.getString("error_load_city"), e);
		}
	}	

		
}
