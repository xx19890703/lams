package com.suun.publics.data.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.suun.publics.data.DataminingStrategy;

public class DataminingMysqlStrategy implements DataminingStrategy {

	private DataSource dataSource;

	private static DataminingStrategy dataHandle = null;

	public static DataminingStrategy getDataHandle() {
		if (dataHandle == null) {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			dataHandle = (DataminingStrategy) ctx.getBean("dataminingMysqlStrategy");
		}
		return dataHandle;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean insertData(String sql) {
		Connection conn = null;
		boolean result = false;
		try {
			conn = dataSource.getConnection();
			result = insertDataBysql(conn, sql);
		} catch (Exception ex) {
			System.out.println(ex);
			return result;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (final SQLException ex) {
					System.out.println(ex);
				}
			}
		}
		return result;
	}

	private boolean insertDataBysql(Connection conn, String sql) throws SQLException {
		Statement statement = null;
		boolean rs = false;
		statement = conn.createStatement();
		String[] sqls = sql.split(";");

		for (String sq : sqls) {
			sq = sq.replaceAll("\\\\", "/");
			statement.addBatch(sq);
		}
		statement.executeBatch();
		return rs;
	}

	@Override
	public List<String> findModeTables(String contractId) {
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();
		String sql = "select * from contract_mode where contractId='" + contractId + "'";
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				String tableName = rs.getString("tableName");
				result.add(tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
					if (statement != null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return result;
				}
		}
		return result;
	}

	@Override
	public List<String> findTableData(String contractId, String tableName) {
		Connection conn = null;
		List<String> result = new ArrayList<String>();
		try {
			conn = dataSource.getConnection();
			result = getTableInsertSql(conn, tableName, " where contractId='" + contractId + "'");

		} catch (Exception ex) {
			System.out.println(ex);
			return result;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (final SQLException ex) {
					System.out.println(ex);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("resource")
	private List<String> getTableInsertSql(Connection conn, String tableName, String where) throws Exception {
		ResultSet rs = null;
		Statement statement = null;
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			rs = metadata.getColumns(null, null, tableName, "%"); // 得到表的字段列表

			String sql = " insert into " + tableName + " ( ";
			int count = 0;
			// 获得列的总数
			while (rs.next()) {
				String colName = rs.getString("column_name");
				String dataType = rs.getString("TYPE_NAME");
				list.add(colName);
				map.put(colName, dataType);
				if (!"DATE".equalsIgnoreCase(dataType)) {
					if (count == 0) {
						sql += colName + " ";
					} else {
						sql += "," + colName + " ";
					}
				}
				count++;
			}
			sql += " ) values ( ";
			// 重新获得列数据 整理成sql
			String dsql = "select * from " + tableName + " " + where;
			statement = conn.createStatement();
			rs = statement.executeQuery(dsql);
			String halfSql = sql;
			while (rs.next()) {
				sql = halfSql;
				boolean isFirst = true;
				for (String names : list) {
					String type = map.get(names);
					if ("DATE".equalsIgnoreCase(type)) {

					} else if ("LONG".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type)) {
						Object value = rs.getObject(names);
						if (isFirst) {
							sql += " " + value + " ";
							isFirst = false;
						} else {
							sql += " ," + value + " ";
						}
					} else {
						Object value = rs.getObject(names);
						if (isFirst) {
							sql += " '" + value + "' ";
							isFirst = false;
						} else {
							sql += " ,'" + value + "' ";
						}
					}
				}
				sql += " );";
				result.add(sql);
			}

		} finally {
			if (rs != null)
				rs.close();
			if (statement != null)
				statement.close();
		}
		return result;
	}

	@Override
	public boolean deleteData(String contractId, String tableName) {
		Connection conn = null;
		boolean rs = false;
		Statement statement = null;
		try {
			conn = dataSource.getConnection();
			String sql = "delete from " + tableName + " where contractId='" + contractId + "' ;";
			statement = conn.createStatement();
			rs = statement.execute(sql);
			return rs;
		} catch (Exception ex) {
			System.out.println(ex);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (final SQLException ex) {
					System.out.println(ex);
				}
			}
		}
		return rs;
	}
}
