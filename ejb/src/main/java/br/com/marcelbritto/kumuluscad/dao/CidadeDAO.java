package br.com.marcelbritto.kumuluscad.dao;

import br.com.marcelbritto.kumuluscad.model.Cidade;

public class CidadeDAO extends AbstractGenericDAO<Cidade, Integer> {

	public CidadeDAO() {
		super(Cidade.class);
	}
}
