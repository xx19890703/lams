/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package com.suun.publics.hibernate.MySQL;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;

/**
 * Constrains a property to between two values
 * @author Gavin King
 */
public class MySQLBetweenExpression implements Criterion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5433005225548210745L;
	private final String propertyName;
	private final Object lo;
	private final Object hi;

	protected MySQLBetweenExpression(String propertyName, Object lo, Object hi) {
		this.propertyName = propertyName;
		this.lo = lo;
		this.hi = hi;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		String[] xcols =criteriaQuery.findColumns(propertyName, criteria);
		for(int i=0;i<xcols.length;i++){
			xcols[i]="convert("+xcols[i]+" using gbk)";
		}
		return StringHelper.join(
			" and ",
			StringHelper.suffix( xcols, " between ? and ?" )
		);

		//TODO: get SQL rendering out of this package!
	}

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		return new TypedValue[] {
				criteriaQuery.getTypedValue(criteria, propertyName, lo),
				criteriaQuery.getTypedValue(criteria, propertyName, hi)
		};
	}

	public String toString() {
		return propertyName + " between " + lo + " and " + hi;
	}

}
