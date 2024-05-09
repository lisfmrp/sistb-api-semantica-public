package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nivel_acesso_propriedade")
public class NivelAcessoPropriedade {

	@Id
	@GeneratedValue
	private int id;
	
	private String propriedadeUri;
	private int nivel = 1;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPropriedadeUri() {
		return propriedadeUri;
	}
	public void setPropriedadeUri(String propriedadeUri) {
		this.propriedadeUri = propriedadeUri;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}	
}
