package br.com.marcelbritto.kumuluscad.service;

import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.jboss.logging.Messages;

import br.com.marcelbritto.kumuluscad.dao.EstadoDAO;
import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.model.Estado;

/**
*
* @author marcelbritto
*
*/
@Stateless
public class EstadoFacade{

	@Inject
	private transient Logger logger;
	
	@Inject
	private EstadoDAO stateDAO;	
	
	private static final String NOME_ARQUIVO_CONSTANTES = "META-INF/ApplicationResources";

	private static ResourceBundle bundle = ResourceBundle.getBundle(NOME_ARQUIVO_CONSTANTES);

	/**
	 * Retorna a lista de estados.
	 *
	 * @return List<Estado> {@link Estado}
	 */
	public List<Estado> listStates() throws BusinessException {
		try {
			return stateDAO.getList();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new BusinessException(bundle.getString("error_load_states"), e);
		}

	}
	
	/**
	 * Retorna o estado.
	 *
	 * @return List<Estado> {@link Estado}
	 */
	public Estado getState(Integer id) throws BusinessException {
		try {
			return stateDAO.find(id);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new BusinessException(bundle.getString("error_load_state"), e);
		}

	}	

		
}
