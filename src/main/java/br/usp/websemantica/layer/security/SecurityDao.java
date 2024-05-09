package br.usp.websemantica.layer.security;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.model.BlocklistPropriedade;
import br.usp.websemantica.model.NivelAcessoEntidade;
import br.usp.websemantica.model.NivelAcessoPropriedade;
import br.usp.websemantica.model.SistemaExterno;
import br.usp.websemantica.model.TokenSisTB;

@Repository
@Transactional(rollbackFor={Exception.class})
public class SecurityDao {
	
	@Autowired
	@Qualifier("sessionFactoryDefault")
	private SessionFactory sessionFactory;
	
	public void saveToken(TokenSisTB token) {
		Session session = this.sessionFactory.getCurrentSession();		
		session.save(token);		
	}
	
	public void updateToken(TokenSisTB token) {
		Session session = this.sessionFactory.getCurrentSession();		
		session.update(token);		
	}
	
	public TokenSisTB getTokenById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TokenSisTB.class);
		criteria.add(Restrictions.eq("id", id));
		return (TokenSisTB) criteria.uniqueResult();
	}
	
	public void updateSistemaExterno(SistemaExterno sistemaExterno) {
		Session session = this.sessionFactory.getCurrentSession();		
		session.update(sistemaExterno);		
	}
	
	public SistemaExterno getSistemaExternoByApiKey(String apiKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SistemaExterno.class);
		criteria.add(Restrictions.eq("bloqueado", 0));
		criteria.add(Restrictions.eq("apiKey", apiKey));
		return (SistemaExterno) criteria.uniqueResult();
	}
	
	public SistemaExterno getSistemaExternoByConfigKey(String configKey) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SistemaExterno.class);
		criteria.add(Restrictions.eq("bloqueado", 0));
		criteria.add(Restrictions.eq("configKey", configKey));
		return (SistemaExterno) criteria.uniqueResult();
	}
	
	public int getNivelMaxPropriedade(String propriedadeUri) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NivelAcessoPropriedade.class);
		criteria.add(Restrictions.eq("propriedadeUri", propriedadeUri));
		criteria.addOrder(Order.desc("nivel"));
		
		@SuppressWarnings("unchecked")
		List<NivelAcessoPropriedade> n = (List<NivelAcessoPropriedade>) criteria.list();
		if(!n.isEmpty()) {
			return n.get(0).getNivel();
		}
		
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	public List<NivelAcessoPropriedade> getNivelAcessoPropriedades() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NivelAcessoPropriedade.class);
		return (List<NivelAcessoPropriedade>) criteria.list();
	}
	
	public int getNivelAcessoEntidade(String entidade) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(NivelAcessoEntidade.class);
		criteria.add(Restrictions.eq("entidade", entidade));
		
		NivelAcessoEntidade n = (NivelAcessoEntidade) criteria.uniqueResult();
		if(n != null) {
			return n.getNivel();
		}
		
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPropriedadesBloqueadasBySistemaExterno(int idSistemaExterno) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BlocklistPropriedade.class);
		criteria.add(Restrictions.eq("idSistemaExterno", idSistemaExterno));
		List<BlocklistPropriedade> blocklist = (List<BlocklistPropriedade>) criteria.list();
		
		List<String> propriedadesBloqueadas = new ArrayList<String>();
		for (BlocklistPropriedade blocklistPropriedade : blocklist) {
			propriedadesBloqueadas.add(blocklistPropriedade.getPropriedadeUri());
		}
		return propriedadesBloqueadas;
	}
}
