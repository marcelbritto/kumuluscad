package br.com.marcelbritto.kumuluscad.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "pessoa")
@Data
@NamedQueries({ 
	@NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p"),
	@NamedQuery(name = "Pessoa.findById", query = "SELECT p FROM Pessoa p WHERE p.id = :id"),
	@NamedQuery(name = "Pessoa.findByNomeLike", query = "SELECT p FROM Pessoa p WHERE p.nome LIKE :nome")
	})
public class Pessoa extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -119764505141197106L;
		
	
	@NotNull
	@Size(max = 150)
	private String nome;
	
	@NotNull
	private String cpf;
	
	@Temporal(TemporalType.DATE)
	private Date idade;
	
	@Size(max = 2)
	private String sexo;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pessoa", orphanRemoval = true)
	private Set<Endereco> enderecos;
	
		
}
