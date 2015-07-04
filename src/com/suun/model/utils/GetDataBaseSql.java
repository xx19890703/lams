package com.suun.model.utils;
/**
 * 传入合同id、表名，返回insert语句
 * @author renlq
 *
 */
public interface GetDataBaseSql {

	public String getDataSqlByContractId(String contractid,String tablename);
}
