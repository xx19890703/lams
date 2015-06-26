package com.suun.publics.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.internal.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.suun.publics.hibernate.FilterInfo.Logic;
import com.suun.publics.hibernate.MySQL.MySQLOrder;
import com.suun.publics.hibernate.MySQL.MySQLRestrictions;
import com.suun.publics.utils.BeanUtils;
import com.suun.publics.utils.Utils;



/**
 * Hibernate的范型基类.
 * 
 * 可以在service类中直接创建使用.也可以继承出DAO子类,在多个Service类中共享DAO操作.
 * 参考Spring2.5自带的Petlinc例子,取消了HibernateTemplate,直接使用Hibernate原生API.
 * 通过Hibernate的sessionFactory.getCurrentSession()获得session.
 *
 * @param <T> DAO操作的对象类型

 * @param <PK> 主键类型
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleHibernateTemplate<T, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;
	 

	public SimpleHibernateTemplate(SessionFactory sessionFactory, Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(T entity) {
		Assert.notNull(entity);
		//为了解决OneToMany级联更新Many方表问题
		//getSession().clear();
		getSession().saveOrUpdate(entity);
	}

	public void delete(T entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
	}

	public void delete(PK id) {
		Assert.notNull(id);
		getSession().delete(get(id));
	}
	
	public void executeSQL(String sql) {
		getSession().createSQLQuery(sql).executeUpdate();
		getSession().flush();
	}

	public List<T> findAll() {
		return findByCriteria();
	}
	public List<T> findAll(Condition condition) {
		return findByCriteria(condition);
	}
    
	public List<T> findAll(Order... order ) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Order o : order) {
			criteria.addOrder(o);
		}
		return criteria.list();
	}
	
	public Page<T> findAll(Page<T> page) {
		return findByCriteria(page);
	}
	 

	/**
	 * 按id获取对象.
	 */
	public T get(final PK id) {
		return (T) getSession().load(entityClass, id);
	}

	public T get(int id) {
		return (T) getSession().load(entityClass, id);
	}	

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param hql hql语句
	 * @param values 数量可变的参数

	 */
	@SuppressWarnings("rawtypes")
	public List find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}


	public void executeHql(String hql, Object... values) {
		executeQuery(hql, values);
	}
 
	/**
	 * 按HQL查询唯一对象.
	 */
	public Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询Intger类形结果. 
	 */
	public Integer findInt(String hql, Object... values) {
		return (Integer) findUnique(hql, values);
	}

	/**
	 * 按HQL查询Long类型结果. 
	 */
	public Long findLong(String hql, Object... values) {
		return (Long) findUnique(hql, values);
	}
	/**条件查询
	 * 按Criterion查询对象列表.
	 * @param criterion 数量可变的Criterion.
	 */
	public List<T> findByCriteria(Condition condition,Criterion... criterion) {
		Criteria c = createCriteria(criterion);   
		//处理OrderBys
		c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		c=doOrderBys(condition,c);
		//处理Filters
		c=doFilters(condition,c);
		return c.list();
	}
	
	
	/**
	 * 按Criterion查询对象列表.
	 * @param criterion 数量可变的Criterion.
	 */
	public List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}
	//判断关联是否已经存在，解决duplicate association path异常	
	private Criteria getAlias(Criteria c,String name){
		CriteriaImpl ct=(CriteriaImpl) c;
		Iterator<Subcriteria> subctiteria=ct.iterateSubcriteria();
		while (subctiteria.hasNext()){
			Subcriteria sct=subctiteria.next();
			if (sct.getAlias().equals(name)){
				return sct;
			}
		}
		return null;		
	}

	private Criteria doOrderBys(Condition condition,Criteria c){
		Criteria c1=null;
		String sFieldName=null;
		if (condition.getOrderBys()!=null){
			for(int i=0;i<condition.getOrderBys().size();i++){
				OrderBy orderBy=(OrderBy)condition.getOrderBys().get(i);
				sFieldName=orderBy.getFieldName();
				c1 =c;
				String[] as = sFieldName.split("[.]");
				if (!this.getIdName().equals(as[0])){
					if ((as != null) && (as.length > 1)) { 
						for (int j = 0; j < as.length - 1; j++) {
							//判断关联是否已经存在，解决duplicate association path异常		
							Criteria tc=getAlias(c,as[j]);
	    					if (tc==null) {
					        	c1=c1.createCriteria(as[j], as[j]);
					        }else{
					        	c1=tc;
					        }
					    }
						sFieldName=as[as.length-1];
					}
				}				
				String dialect = 
						((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();  
				//解决MySQL中文排序问题
				if (dialect.contains("MySQL")){
					if (orderBy.isAsc()){        						
						c1.addOrder(MySQLOrder.asc(sFieldName));
					} else {
						c1.addOrder(MySQLOrder.desc(sFieldName));
					}    						
				} else {
					if (orderBy.isAsc()){        						
						c1.addOrder(Order.asc(sFieldName));
					} else {
						c1.addOrder(Order.desc(sFieldName));
					}
				}
			}
		}
		return c;
	}
	private Criteria doFilters(Condition condition,Criteria c){
		String dialect = 
				((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
		Criteria c1=null;
		String sFieldName=null;
		if (condition.getFilterInfos()!=null){
			Criterion allc=null;
			for(int i=0;i<condition.getFilterInfos().size();i++){				
				Criterion cone=null;
				com.suun.publics.hibernate.FilterInfo filterInfo=(com.suun.publics.hibernate.FilterInfo)condition.getFilterInfos().get(i);
				if (filterInfo.getLogic().equals(Logic.SQL)){
					cone=Restrictions.sqlRestriction((String)filterInfo.getValue());
				} else {
					c1 =c;
					sFieldName=filterInfo.getFieldName();
					String[] as = sFieldName.split("[.]");
					if (!this.getIdName().equals(as[0])){
						if ((as != null) && (as.length > 1)) { 
							for (int j = 0; j < as.length - 1; j++) {
								//判断关联是否已经存在，解决duplicate association path异常		
								Criteria tc=getAlias(c,as[j]);
		    					if (tc==null) {
						        	c1=c1.createCriteria(as[j], as[j]);
						        }else{
						        	c1=tc;
						        }
						    }
						}
					}					
					boolean mySqlString=dialect.contains("MySQL")&&filterInfo.getValue().getClass()==String.class;
					if (filterInfo.getLogic().equals(Logic.EQUAL)){
						if (mySqlString){
							cone=MySQLRestrictions.eq(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.eq(sFieldName, filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.NOTEQUAL)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.ne(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.ne(sFieldName, filterInfo.getValue());
						}   
					} else if (filterInfo.getLogic().equals(Logic.GREAT)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.gt(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.gt(sFieldName, filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.GREATEQUAL)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.ge(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.ge(sFieldName, filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.LESS)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.lt(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.lt(sFieldName, filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.LESSEQUAL)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.le(sFieldName, filterInfo.getValue());
						} else{
							cone=Restrictions.le(sFieldName, filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.LLIKE)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.like(sFieldName, filterInfo.getValue()+"%");
						} else{
							cone=Restrictions.like(sFieldName, filterInfo.getValue()+"%");
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.RLIKE)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.like(sFieldName, "%"+filterInfo.getValue());
						} else{
							cone=Restrictions.like(sFieldName, "%"+filterInfo.getValue());
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.LIKE)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.like(sFieldName, "%"+filterInfo.getValue()+"%");
						} else{
							cone=Restrictions.like(sFieldName, "%"+filterInfo.getValue()+"%");
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.ISNULL)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.isNull(sFieldName);
						} else{
							cone=Restrictions.isNull(sFieldName);
						}	    						
					} else if (filterInfo.getLogic().equals(Logic.ISNOTNULL)){
                        if (mySqlString){
                        	cone=MySQLRestrictions.isNotNull(sFieldName);
						} else{
							cone=Restrictions.isNotNull(sFieldName);
						}						
					}
				}
				if (allc==null){
					allc=cone;
				}else{
					com.suun.publics.hibernate.FilterInfo prefilterInfo=(com.suun.publics.hibernate.FilterInfo)condition.getFilterInfos().get(i-1);
					if (prefilterInfo.isOr()){
						//if (filterInfo.isOr()){
						if (dialect.contains("MySQL")){
							allc=MySQLRestrictions.or(cone,allc);
						} else{
							allc=Restrictions.or(cone,allc);
						}
					}else{
						if (dialect.contains("MySQL")){
							allc=MySQLRestrictions.and(cone,allc);
						} else{
							allc=Restrictions.and(cone,allc);
						}
					}						
				}
			}
			if (allc!=null)
			    c.add(allc);
		}		
		return c;
	}
	
	//处理当新添加记录时，重新计算当前页位置
	@SuppressWarnings("rawtypes")
	private void doNewResPos(Page page, Criterion... criterion) {
		String dialect = ((SessionImpl) getSession()).getFactory().getDialect()
				.getClass().getName();
		Criteria c1 = null;
		String sFieldName = null;
		page.setCurPagePos(-1);
		if (page.getPosionRes() != null) {
			Criteria cpos = createCriteria(criterion);
			cpos = doOrderBys(page.getCondition(), cpos);
			cpos = doFilters(page.getCondition(), cpos);
			T entity = (T) page.getPosionRes();
			boolean ord = false;
			if (page.getCondition().getOrderBys() != null) {
				ord = page.getCondition().getOrderBys().size() > 0;
			}
			if (ord) {
				for (int i = 0; i < page.getCondition().getOrderBys().size(); i++) {
					OrderBy orderBy = (OrderBy) page.getCondition().getOrderBys().get(i);
					sFieldName = orderBy.getFieldName();
					c1 = cpos;
					Object pkvalue = null;
					Object entity1 = entity;
					String[] as = sFieldName.split("[.]");
					if (!this.getIdName().equals(as[0])){
						if ((as != null) && (as.length > 1)) {
							for (int j = 0; j < as.length - 1; j++) {
								// 判断关联是否已经存在，解决duplicate association path异常
								Criteria tc = getAlias(cpos, as[j]);
								if (tc == null) {
									c1 = c1.createCriteria(as[j], as[j]);
								} else {
									c1 = tc;
								}
								try {
									entity1 = Utils.invokeMethod(entity1, Utils.field2GetMethod(as[j]), new Class[] {});
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							sFieldName = as[as.length - 1];
						}
						try {
							pkvalue = Utils.invokeMethod(entity1, Utils.field2GetMethod(sFieldName), new Class[] {});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						if ((as != null) && (as.length > 0)) {
							for (int j = 0; j < as.length; j++) {
								try {
									entity1 = Utils.invokeMethod(entity1, Utils.field2GetMethod(as[j]), new Class[] {});
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						pkvalue = entity1;						
					}
					
					if (pkvalue != null) {
						if (orderBy.isAsc()) {
							if (dialect.contains("MySQL")) {
								c1.add(MySQLRestrictions
										.le(sFieldName, pkvalue));
							} else {
								c1.add(Restrictions.le(sFieldName, pkvalue));
							}
						} else {
							if (dialect.contains("MySQL")) {
								c1.add(MySQLRestrictions
										.ge(sFieldName, pkvalue));
							} else {
								c1.add(Restrictions.ge(sFieldName, pkvalue));
							}
						}
					}
				}
			} else { // 没有排序，默认主键排序，主键为符合主键呢？？？？？待验证
				String PKName = null;
				// 如果注解存放在方法上则从方法中存取
				Method[] methods = entity.getClass().getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(javax.persistence.Id.class)||method.isAnnotationPresent(javax.persistence.EmbeddedId.class)) {
						PKName = method.getName().substring(3, 4).toLowerCase()
								+ method.getName().substring(4);
						break;
					}
				}
				// 如果注解是在字段上
				if (PKName == null) {
					Field[] fields = entity.getClass().getFields();
					for (Field field : fields) {
						if (field.isAnnotationPresent(javax.persistence.Id.class)||field.isAnnotationPresent(javax.persistence.EmbeddedId.class)) {
							PKName = field.getName();
							break;
						}
					}
				}

				if (PKName != null) {
					String PKMethodName = Utils.field2GetMethod(PKName);
					Object pkvalue = null;
					try {
						pkvalue = Utils.invokeMethod(entity, PKMethodName,
								new Class[] {});
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (pkvalue != null) {
						cpos.add(Restrictions.le(PKName, pkvalue));
					}
				}
			}
			long totalCount = (Long) cpos.setProjection(Projections.rowCount())
					.uniqueResult();
			page.setCurPagePos(totalCount % page.getPageSize() > 0 ? totalCount
					% page.getPageSize() : page.getPageSize());
			page.setPageNo(totalCount / page.getPageSize()
					+ (totalCount % page.getPageSize() > 0 ? 1 : 0));
		}
		return;
	}
	/**
	 * 按Criterion分页查询.
	 * @param page 分页参数.包括pageSize、firstResult、orderBys、filterInfos、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 * @param criterion 数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public Page<T> findByCriteria(Page<T> page, Criterion... criterion) {
		Assert.notNull(page);

		Criteria c = createCriteria(criterion);
		//处理OrderBys
		c=doOrderBys(page.getCondition(),c);
		//处理Filters
		c=doFilters(page.getCondition(),c);
		//处理当新添加记录时，重新计算当前页位置		
		doNewResPos(page, criterion);

		if (page.isAutoCount()) {
			page.setTotalCount(countQueryResult(page, c));
			long rTotalpage=page.getTotalCount()/page.getPageSize()+(page.getTotalCount()%page.getPageSize()>0?1:0);
			if (rTotalpage<page.getPageNo())
				page.setPageNo(rTotalpage);				
		}
		if (page.isFirstSetted()) {
			c.setFirstResult((int) page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}
		page.setResult(c.list());
		return page;
	}
	 
	/**
	 * 按属性查找对象列表.
	 */
	public List<T> findByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 按属性查找唯一对象.
	 */
	public T findUniqueByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
	}
	
	/**
	 * 根据执行函数与参数列表
	 */
	public int executeQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.executeUpdate();
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	public Criteria createCriteria(Criterion... criterions) {
		//System.out.println("实体类名----------------------------->"+entityClass.getName().toString());
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原值(orgValue)则不作比较.
	 * 传回oldValue的设计侧重于从页面上发出Ajax判断请求的场景.
	 * 否则需要SS2里那种以对象ID作为第3个参数的isUnique函数.
	 */
	public boolean isUnique(String propertyName, Object value, Object oldValue) {
		if (value == null || value.equals(oldValue))
			return true;

		Object object = findUniqueByProperty(propertyName, value);
		if (object == null)
			return true;
		else
			return false;
	}
	
	/**
	 * 判断对象的主键值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果主键新修改的值(value)等于属性原值(orgValue)则不作比较.
	 * 传回oldValue的设计侧重于从页面上发出Ajax判断请求的场景.
	 * 否则需要SS2里那种以对象ID作为第3个参数的isUnique函数.
	 */
	public boolean isIdUnique(Object value, Object oldValue) {
		if (value == null || value.equals(oldValue))
			return true;

		Object object = findUniqueByProperty(getIdName(), value);
		if (object == null)
			return true;
		else
			return false;
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * @return page对象中的totalCount属性将赋值.
	 */
	@SuppressWarnings("rawtypes")
	protected long countQueryResult(Page<T> page, Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、OrderBy、ResultTransformer取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();

		ResultTransformer transformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl, "orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		// 执行Count查询
		long totalCount =  (Long) c.setProjection(Projections.rowCount()).uniqueResult();;
		if (totalCount < 1)
			return -1;

		// 将之前的Projection和OrderBy条件重新设回去

		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		if (transformer != null) {
			c.setResultTransformer(transformer);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		return totalCount;
	}
	
    /** 
     * 取得对象的主键名. 
     */  
    public String getIdName() {  
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);  
        return meta.getIdentifierPropertyName();  
    }

}