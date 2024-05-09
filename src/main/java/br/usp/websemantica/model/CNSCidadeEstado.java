package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vw_cadsus_cidade_estado")
public class CNSCidadeEstado {

	@Id
	@GeneratedValue
	private int idCidade;
	
	private String cidade;
	private int codIbge;
	private String estado;
	private String uf;
	
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public int getIdCidade() {
		return idCidade;
	}
	public int getCodIbge() {
		return codIbge;
	}
	public void setCodIbge(int codIbge) {
		this.codIbge = codIbge;
	}

}
