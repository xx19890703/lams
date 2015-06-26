package com.suun.service.professor;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.professor.Professor;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

@Service
@Transactional
public class ProfessorManager {

	private SimpleHibernateTemplate<Professor, String> proDao;

		@Autowired
		public void setSessionFactory(SessionFactory sessionFactory) {
			proDao = new SimpleHibernateTemplate<Professor, String>(sessionFactory, Professor.class);
		}
		
		@Transactional(readOnly = true)
		public Professor getProfessor(String id) {
			return proDao.get(id);
		}
		
		@Transactional(readOnly = true)
		public List<Professor> getAllProfessors() {
			return proDao.findAll();
		}
		
		@Transactional(readOnly = true)
		public Page<Professor> getAllProfessors(Page<Professor> page) {
			return proDao.findAll(page);
		}
		
		@Transactional(readOnly = true)
		public List<Professor> getAllProfessors(Condition condition) {
			return proDao.findAll(condition);
		}

		public void saveProfessor(Professor resource) {
			proDao.save(resource);
		}

		public void deleteProfessor(String id) {
			Professor resource = proDao.get(id);
			proDao.delete(resource);
		}

		@Transactional(readOnly = true)
		public boolean isIdUnique(String id, String orgId) {
			return proDao.isUnique("id", id, orgId);
		}

}
