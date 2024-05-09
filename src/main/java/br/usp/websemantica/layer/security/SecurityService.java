package br.usp.websemantica.layer.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.model.NivelAcessoPropriedade;
import br.usp.websemantica.model.SistemaExterno;
import br.usp.websemantica.model.TokenSisTB;

@Component
@Transactional(rollbackFor={Exception.class})
public class SecurityService {

	@Autowired
	private SecurityDao securityDao;
	
	public void saveToken(TokenSisTB token) {
		this.securityDao.saveToken(token);
	}
	
	public void updateToken(TokenSisTB token) {
		this.securityDao.updateToken(token);
	}
	
	public TokenSisTB getTokenById(int id) {
		return this.securityDao.getTokenById(id);
	}
	
	public void updateSistemaExterno(SistemaExterno sistemaExterno) {
		this.securityDao.updateSistemaExterno(sistemaExterno);
	}
	
	public SistemaExterno getSistemaExternoByApiKey(String apiKey) {
		return this.securityDao.getSistemaExternoByApiKey(apiKey);
	}
	
	public SistemaExterno getSistemaExternoByConfigKey(String configKey) {
		return this.securityDao.getSistemaExternoByConfigKey(configKey);
	}
	
	public int getNivelMaxPropriedade(String propriedadeUri) {
		return this.securityDao.getNivelMaxPropriedade(propriedadeUri);
	}
	
	public List<NivelAcessoPropriedade> getNivelAcessoPropriedades() {
		return this.securityDao.getNivelAcessoPropriedades();
	}
	
	public int getNivelAcessoEntidade(String entidade) {
		return this.securityDao.getNivelAcessoEntidade(entidade);
	}
	
	public List<String> getPropriedadesBloqueadasBySistemaExterno(int idSistemaExterno) {
		return this.securityDao.getPropriedadesBloqueadasBySistemaExterno(idSistemaExterno);
	}
}
