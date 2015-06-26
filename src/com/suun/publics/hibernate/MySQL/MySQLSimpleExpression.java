package com.suun.publics.hibernate.MySQL;

import java.sql.Types;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class MySQLSimpleExpression extends SimpleExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9010527556582203680L;
	private final String propertyName;
	private final Object value;
	private boolean ignoreCase;
	@SuppressWarnings("unused")
	private final String op;
	
	protected MySQLSimpleExpression(String propertyName, Object value, String op) {
		super(propertyName,value, op);
		this.propertyName = propertyName;
		this.value = value;
		this.op = op;
	}
	
	protected MySQLSimpleExpression(String propertyName, Object value, String op, boolean ignoreCase) {
		super(propertyName,value, op, ignoreCase);
		this.propertyName = propertyName;
		this.value = value;
		this.ignoreCase = ignoreCase;
		this.op = op;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
				String[] columns = criteriaQuery.findColumns(propertyName, criteria);
				Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
				StringBuffer fragment = new StringBuffer();
				if (columns.length>1) fragment.append('(');
				SessionFactoryImplementor factory = criteriaQuery.getFactory();
				int[] sqlTypes = type.sqlTypes( factory );
				for ( int i=0; i<columns.length; i++ ) {
					boolean lower = ignoreCase && 
							( sqlTypes[i]==Types.VARCHAR || sqlTypes[i]==Types.CHAR );
					if (lower) {
						fragment.append( factory.getDialect().getLowercaseFunction() )
							.append('(');
					}
					fragment.append("convert("+columns[i]+" using gbk)" );
					if (lower) fragment.append(')');
					fragment.append( getOp() ).append("?");
					if ( i<columns.length-1 ) fragment.append(" and ");
				}
				if (columns.length>1) fragment.append(')');
				return fragment.toString();

			}

	public Object getValue() {
		return value;
	}

}
