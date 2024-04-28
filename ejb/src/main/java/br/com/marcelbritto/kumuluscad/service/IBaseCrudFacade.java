package br.com.marcelbritto.kumuluscad.service;

import java.util.List;

import br.com.marcelbritto.kumuluscad.dao.AbstractGenericDAO;
import br.com.marcelbritto.kumuluscad.model.Pessoa;

public interface IBaseCrudFacade<E extends Pessoa, R extends AbstractGenericDAO<E, Integer>> {

	E create(E entity);

	E edit(E entity);

	void delete(E entity);

	void deleteById(Integer id);

	E find(Integer id);

	List<E> findAll();

}