package br.com.marcelbritto.kumuluscad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marcel Britto
 *
 */
public enum Sexo {
	
	M("Masculino"),
	
	F("Feminino");

	private String nome;
	
	private Sexo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public static List <Sexo> getList () {
    	 return new ArrayList<Sexo>(Arrays.asList(Sexo.values()));
    }
	
	public List<Object> constFromEnumToList() {
	    List<Object> nueva = new ArrayList<Object>();
        try {
            Class<?> cls = Class.forName(this.getNome());
            Object[] consts = cls.getEnumConstants();
            nueva.addAll(Arrays.asList(consts));
        } catch (ClassNotFoundException e) {
            System.out.println("Classe não existe");
        }
        return nueva;
	}
}
