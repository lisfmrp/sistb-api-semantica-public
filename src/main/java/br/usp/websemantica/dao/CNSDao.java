package br.usp.websemantica.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.model.CNSCidadeEstado;
import br.usp.websemantica.model.CNSPais;
import br.usp.websemantica.model.CNSRaca;
import br.usp.websemantica.model.CNSTipoLogradouro;
import br.usp.websemantica.model.Tratamento;

@Repository
@Transactional(rollbackFor={Exception.class})
public class CNSDao {
	
	@Autowired
	@Qualifier("sessionFactoryDefault")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<Tratamento> getTratamentoByCodPaciente(int codPaciente) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Tratamento.class);
		criteria.add(Restrictions.eq("codPaciente", codPaciente));
		return (List<Tratamento>) criteria.list();
	}
	
	public CNSRaca getRacaById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CNSRaca.class);
		criteria.add(Restrictions.eq("id", id));
		return (CNSRaca) criteria.uniqueResult();
	}
	
	public CNSPais getPaisById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CNSPais.class);
		criteria.add(Restrictions.eq("id", id));
		return (CNSPais) criteria.uniqueResult();
	}
	
	public CNSTipoLogradouro getTipoLogradouroById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CNSTipoLogradouro.class);
		criteria.add(Restrictions.eq("id", id));
		return (CNSTipoLogradouro) criteria.uniqueResult();
	}
	
	public CNSCidadeEstado getCidadeEstadoByCodIBGE(int codIBGE) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CNSCidadeEstado.class);
		criteria.add(Restrictions.eq("codIbge", codIBGE));
		criteria.setMaxResults(1);
		return (CNSCidadeEstado) criteria.uniqueResult();
	}
}
