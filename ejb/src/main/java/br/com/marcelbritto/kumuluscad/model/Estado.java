package br.com.marcelbritto.kumuluscad.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author sergiorobertojunior
 */
@Entity
@Table(name = "estado")
@NamedQueries({
    @NamedQuery(name = "Estado.findAll", query = "SELECT s FROM Estado s ORDER BY s.nome"),
    @NamedQuery(name = "Estado.findById", query = "SELECT s FROM Estado s WHERE s.id = :id"),
    @NamedQuery(name = "Estado.findByNome", query = "SELECT s FROM Estado s WHERE s.nome = :nome")})
@Data
@Builder
@AllArgsConstructor
public class Estado implements Serializable, Comparable<Estado> {
	
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;

    @Basic(optional = false)
    @Column(name = "initial")
    private String initial;
    
        
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "estado")
    private List<Cidade> cidades;

    public Estado() {
    }

    public Estado(Integer id) {
        this.id = id;
    }

    public Estado(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Estado state) {
		
    	return (this.id == state.id) ? 1 : 0; 
		
	}
}
