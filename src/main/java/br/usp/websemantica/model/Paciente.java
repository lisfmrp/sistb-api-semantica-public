package br.usp.websemantica.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="paciente")
public class Paciente {

	@Id
	@GeneratedValue
	private int codPaciente;
	
	@Column(name="sinan")
	private String nroSinan;
	
	private String cpf;
	
	private String cns;
	
	public int getCodPaciente() {
		return codPaciente;
	}

	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}

	public String getNroSinan() {
		return nroSinan;
	}

	public void setNroSinan(String nroSinan) {
		this.nroSinan = nroSinan;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}
}
