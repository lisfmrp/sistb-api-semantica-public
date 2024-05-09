package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sistema_externo")
public class SistemaExterno {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String alias;
	private String apiKey;
	private String configKey;
	private String chavePublica;	
	private int bloqueado = 0;
	private int nivelMax = 1;
	private int sparql = 0;
	private int graphql = 0;
	private int permitirNovaChavePublica = 0;
	private int cadsus = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(int bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public int getNivelMax() {
		return nivelMax;
	}
	public void setNivelMax(int nivelMax) {
		this.nivelMax = nivelMax;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getSparql() {
		return sparql;
	}
	public void setSparql(int sparql) {
		this.sparql = sparql;
	}
	public int getGraphql() {
		return graphql;
	}
	public void setGraphql(int graphql) {
		this.graphql = graphql;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getChavePublica() {
		return chavePublica;
	}
	public void setChavePublica(String chavePublica) {
		this.chavePublica = chavePublica;
	}
	public int getPermitirNovaChavePublica() {
		return permitirNovaChavePublica;
	}
	public void setPermitirNovaChavePublica(int permitirNovaChavePublica) {
		this.permitirNovaChavePublica = permitirNovaChavePublica;
	}
	public int getCadsus() {
		return cadsus;
	}
	public void setCadsus(int cadsus) {
		this.cadsus = cadsus;
	}
	
}
