package br.com.marcelbritto.kumuluscad.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "pessoa")
@Data
@NamedQueries({ 
	@NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p"),
	@NamedQuery(name = "Pessoa.findById", query = "SELECT p FROM Pessoa p WHERE p.id = :id")
	})
public class Pessoa implements Serializable{

	private static final long serialVersionUID = -119764505141197106L;
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
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
