package br.com.marcelbritto.kumuluscad.service;

import java.util.List;

import br.com.marcelbritto.kumuluscad.dao.PessoaDAO;
import br.com.marcelbritto.kumuluscad.model.Pessoa;

public class PessoaFacade extends BaseCrudFacade<Pessoa, PessoaDAO>{

	
	public List<Pessoa> listByNome(String nome) {
		String[] parameters = { "nome" };
		String[] values = { "%" + nome + "%" };
		return dao.getObjects("Pessoa.findByNomeLike", parameters, values);
	}
	
	
}
