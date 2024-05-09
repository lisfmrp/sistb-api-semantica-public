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

import br.usp.websemantica.model.Paciente;
import br.usp.websemantica.model.Tratamento;

@Repository
@Transactional(rollbackFor={Exception.class})
public class Dao {
	
	@Autowired
	@Qualifier("sessionFactorySistb")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<Tratamento> getTratamentoByCodPaciente(int codPaciente) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Tratamento.class);
		criteria.add(Restrictions.eq("codPaciente", codPaciente));
		return (List<Tratamento>) criteria.list();
	}
	
	public Paciente getPaciente(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Paciente.class);
		criteria.add(Restrictions.or(Restrictions.eq("nroSinan", id),Restrictions.eq("cpf", id),Restrictions.eq("cns", id),Restrictions.eq("codPaciente", Integer.parseInt(id))));
		return (Paciente) criteria.uniqueResult();
	}
}
