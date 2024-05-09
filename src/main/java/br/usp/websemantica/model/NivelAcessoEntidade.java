package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nivel_acesso_entidade")
public class NivelAcessoEntidade {

	@Id
	@GeneratedValue
	private int id;
	
	private String entidade;
	private int nivel;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEntidade() {
		return entidade;
	}
	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
}
