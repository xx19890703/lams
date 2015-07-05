package com.suun.service.softline;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.model.softline.Softlinee;
import com.suun.model.softline.SoftlineeReview;
import com.suun.model.system.Dic_data;
import com.suun.model.system.Dic_datakey;
import com.suun.publics.hibernate.Condition;
import com.suun.publics.hibernate.Page;
import com.suun.publics.hibernate.SimpleHibernateTemplate;

@Service
@Transactional
public class SoftlineeReviewManager {

	private SimpleHibernateTemplate<SoftlineeReview, Long> slrDao;
	private SimpleHibernateTemplate<Softlinee, String> slDao;

		@Autowired
		public void setSessionFactory(SessionFactory sessionFactory) {
			slrDao = new SimpleHibernateTemplate<SoftlineeReview, Long>(sessionFactory, SoftlineeReview.class);
			slDao = new SimpleHibernateTemplate<Softlinee, String>(sessionFactory, Softlinee.class);
		}
		
		@Transactional(readOnly = true)
		public SoftlineeReview getSoftlineeReview(String id) {
			return slrDao.get(Long.valueOf(id));
		}
		
		@Transactional(readOnly = true)
		public List<SoftlineeReview> getAllSoftlineeReviews() {
			return slrDao.findAll();
		}
		
		@Transactional(readOnly = true)
		public Page<SoftlineeReview> getAllSoftlineeReviews(Page<SoftlineeReview> page) {
			return slrDao.findAll(page);
		}
		
		@Transactional(readOnly = true)
		public List<SoftlineeReview> getAllSoftlineeReviews(Condition condition) {
			return slrDao.findAll(condition);
		}
		
		@Transactional(readOnly = true)
		public boolean isProUnique(String proId, String orgId) {
			return slrDao.isUnique("proId", proId, orgId);
		}

		public void saveSoftlineeReview(SoftlineeReview resource) {
			Softlinee sl=slDao.get(resource.getSoftlineeInfo().getBaseId());
			Dic_datakey dk=new Dic_datakey();
			dk.setData_no("1");
			dk.setDic_no("SLSTATE");
			Dic_data dd=new Dic_data();
			dd.setKey(dk);
			sl.setSlstate(dd); 
			slDao.save(sl);
			slrDao.save(resource);			
		}

		public void deleteSoftlineeReview(String id) {
			SoftlineeReview resource = slrDao.get(Long.valueOf(id));
			slrDao.delete(resource);
		}

		@Transactional(readOnly = true)
		public boolean isIdUnique(String verdictId, String orgId) {
			return slrDao.isUnique("verdictId", Long.valueOf(verdictId), Long.valueOf(orgId));
		}
		
		@Transactional(readOnly = true)
		public SoftlineeReview getSoftlineeReviewByBaseId(String baseId) { 
			Softlinee softlinee=new Softlinee();
			softlinee.setBaseId(baseId);
			List<SoftlineeReview> slrs=slrDao.findByProperty("softlineeInfo", softlinee);
			if (slrs.size()>0){
				return slrs.get(0);
			} else{
				return null;
			}			 
		}

}
