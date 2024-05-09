package br.usp.websemantica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.dao.CNSDao;
import br.usp.websemantica.model.CNSCidadeEstado;
import br.usp.websemantica.model.CNSPais;
import br.usp.websemantica.model.CNSRaca;
import br.usp.websemantica.model.CNSTipoLogradouro;
import br.usp.websemantica.model.Tratamento;

@Component
@Transactional(rollbackFor={Exception.class})
public class CNSService {

	@Autowired
	private CNSDao dao;
	
	public List<Tratamento> getTratamentoByCodPaciente(int codPaciente) {
		return this.dao.getTratamentoByCodPaciente(codPaciente);
	}
	
	public CNSRaca getRacaById(int id) {
		return this.dao.getRacaById(id);
	}

	public CNSPais getPaisById(int id) {
		return this.dao.getPaisById(id);
	}
	
	public CNSTipoLogradouro getTipoLogradouroById(int id) {
		return this.dao.getTipoLogradouroById(id);
	}
	
	public CNSCidadeEstado getCidadeEstadoByCodIBGE(int codIBGE) {
		return this.dao.getCidadeEstadoByCodIBGE(codIBGE);
	}
}
