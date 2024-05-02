package br.com.marcelbritto.kumuluscad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "endereco")
@Data
public class Endereco extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1180564120437010709L;
			
	@Size(max = 2)
	private String estado;
	
	@Size(max = 100)
	private String cidade;
	
	@Size(max = 100)
	private String logradouro;
	
	@Size(max = 8)
	private String cep;
	
	private Integer numero;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa", insertable = true, updatable = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Pessoa pessoa;
	

}
