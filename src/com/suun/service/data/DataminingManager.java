package com.suun.service.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suun.publics.data.DataminingFactory;
import com.suun.publics.data.DataminingStrategy;

@Service
@Transactional
public class DataminingManager {

	@Autowired
	private DataminingFactory factory;
	

	/**
	 * 根据合同编号，查询与合同相关的表名
	 * @param contractId
	 * @return
	 */
	public List<String> findModeTables(String contractId) {
		DataminingStrategy strategy = factory.getStrategy();
		List<String> result=strategy.findModeTables(contractId);
		return result;
	}

	/**
	 * 根据合同编号，和表名，查询该表导入数据的sql insert。。。。。
	 * @param contractId
	 * @return
	 */
	public List<String> findTableData(String contractId, String tableName) {
		DataminingStrategy strategy = factory.getStrategy();
		List<String> result=strategy.findTableData(contractId,tableName);
		return result;
	}
	
	/**
	 * 根据sql导入数据
	 * @param contractId
	 * @return
	 */
	public boolean insertData(String sql) {
		DataminingStrategy strategy = factory.getStrategy();
		boolean result=strategy.insertData(sql);
		return result;
	}
	
	/**
	 * 根据表名和contractId删除数据
	 * @param contractId
	 * @param tableName
	 * @return
	 */
	public boolean deleteData(String contractId,String tableName){
		DataminingStrategy strategy = factory.getStrategy();
		boolean result=strategy.deleteData(contractId, tableName);
		return result;
	}
}
