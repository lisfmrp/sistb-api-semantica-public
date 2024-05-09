package br.usp.websemantica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.dao.Dao;
import br.usp.websemantica.model.Paciente;
import br.usp.websemantica.model.Tratamento;

@Component
@Transactional(rollbackFor={Exception.class})
public class Service {

	@Autowired
	private Dao dao;
	
	public List<Tratamento> getTratamentoByCodPaciente(int codPaciente) {
		return this.dao.getTratamentoByCodPaciente(codPaciente);
	}
	
	public Paciente getPaciente(String id) {
		return this.dao.getPaciente(id);
	}	
}
