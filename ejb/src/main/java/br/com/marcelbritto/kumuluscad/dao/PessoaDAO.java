package br.com.marcelbritto.kumuluscad.dao;

import java.io.Serializable;

import br.com.marcelbritto.kumuluscad.model.Pessoa;

public class PessoaDAO extends AbstractGenericDAO<Pessoa, Integer> implements Serializable{

	private static final long serialVersionUID = -1916290825328284103L;
	
	public PessoaDAO(Class<Pessoa> entityClass) {
        super(entityClass);
	}
	
	public PessoaDAO() {
        super(Pessoa.class);
	}

}
