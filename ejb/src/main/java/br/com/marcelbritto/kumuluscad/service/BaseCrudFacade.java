/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marcelbritto.kumuluscad.service;

import java.util.List;

import javax.inject.Inject;

import br.com.marcelbritto.kumuluscad.dao.AbstractGenericDAO;
import br.com.marcelbritto.kumuluscad.model.Pessoa;

/**
 *
 * @author root
 */
public class BaseCrudFacade<E extends Pessoa, R extends AbstractGenericDAO<E, Integer>> implements IBaseCrudFacade<E, R> {
    
	@Inject
	protected R dao;
    
    @Override
	public E create(E entity) {
    	return dao.save(entity);
    }

    @Override
	public E edit(E entity) {
    	return dao.update(entity);
    }

    @Override
	public void delete(E entity) {
        dao.delete(entity);
    }
    
    @Override
	public void deleteById(Integer id) {
        dao.deleteById(id);
    }

    @Override
	public E find(Integer id) {
        return dao.find(id);
    }

    @Override
	public List<E> findAll() {
        return dao.getList();
    }

        
}
