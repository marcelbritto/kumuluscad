package br.com.marcelbritto.kumuluscad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "endereco")
@Data
public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1180564120437010709L;
	
	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Size(max = 2)
	private String estado;
	
	@Size(max = 100)
	private String cidade;
	
	@NotNull
	@Size(max = 100)
	private String logradouro;
	
	@Size(max = 8)
	private String cep;
	
	private Integer numero;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa", insertable = true, updatable = true)
	private Pessoa pessoa;
	

}
