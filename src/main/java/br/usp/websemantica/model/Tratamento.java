package br.usp.websemantica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tratamento")
public class Tratamento {

	@Id
	@GeneratedValue
	private int codTratamento;
	
	private int codPaciente;

	public int getCodTratamento() {
		return codTratamento;
	}

	public void setCodTratamento(int codTratamento) {
		this.codTratamento = codTratamento;
	}

	public int getCodPaciente() {
		return codPaciente;
	}

	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}
}
