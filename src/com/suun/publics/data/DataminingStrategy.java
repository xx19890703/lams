package com.suun.publics.data;

import java.util.List;

public interface DataminingStrategy {

	//根据合同id，获取所有的表名称（应该返回insert语句）
	List<String> findModeTables(String contractId);

	//根据合同id、tablename，获取所有的表数据
	List<String> findTableData(String contractId, String tableName);
	
	boolean insertData(String sql);

	//删除 tableName表中 所有 contractId为 contractId的内容 
	boolean deleteData(String contractId, String tableName);
}
