package br.usp.websemantica.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="token")
public class TokenSisTB {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String token;	
	private int utilizado = 0;
	private Date dataCriacao = new Date();
	private Date dataUtilizacao;
	
	@Transient
	private String tokenBase64;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUtilizado() {
		return utilizado;
	}
	public void setUtilizado(int utilizado) {
		this.utilizado = utilizado;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Date getDataUtilizacao() {
		return dataUtilizacao;
	}
	public void setDataUtilizacao(Date dataUtilizacao) {
		this.dataUtilizacao = dataUtilizacao;
	}
	public String getTokenBase64() {
		return tokenBase64;
	}
	public void setTokenBase64(String tokenBase64) {
		this.tokenBase64 = tokenBase64;
	}
}
