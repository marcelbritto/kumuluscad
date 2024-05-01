package br.com.marcelbritto.kumuluscad.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;

import br.com.marcelbritto.kumuluscad.dao.PessoaDAO;
import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Estado;
import br.com.marcelbritto.kumuluscad.model.Pessoa;

@Stateless
public class PessoaFacade {

	@Inject
	private PessoaDAO dao;
	
	@Transactional
	public Pessoa create(Pessoa entity) {
    	return dao.save(entity);
    }

	@Transactional
    public Pessoa update(Pessoa entity) {
    	return dao.update(entity);
    }

    @Transactional
    public void delete(Pessoa entity) {
        dao.delete(entity);
    }
    
    @Transactional
    public void deleteById(Integer id) {
        dao.deleteById(id);
    }

    public Pessoa find(Integer id) {
        return dao.find(id);
    }

    public List<Pessoa> findAll() {
        return dao.getList();
    }
	
	public List<Pessoa> listByNome(String nome) {
		String[] parameters = { "nome" };
		String[] values = { "%" + nome + "%" };
		return dao.getObjects("Pessoa.findByNomeLike", parameters, values);
	}
	
	public List<Cidade> listCidades(Estado estado) {
		Hibernate.initialize(estado.getCidades());
		return estado.getCidades();
	}
	
	
}
