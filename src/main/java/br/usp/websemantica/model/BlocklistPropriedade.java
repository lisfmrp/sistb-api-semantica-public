package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="propriedade_blocklist")
public class BlocklistPropriedade {

	@Id
	@GeneratedValue
	private int id;
	
	private String propriedadeUri;
	private int idSistemaExterno;
	
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
	public int getIdSistemaExterno() {
		return idSistemaExterno;
	}
	public void setIdSistemaExterno(int idSistemaExterno) {
		this.idSistemaExterno = idSistemaExterno;
	}	
}
