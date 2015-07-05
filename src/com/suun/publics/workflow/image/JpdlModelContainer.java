/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package com.suun.publics.workflow.image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yangtao
 * @date 2011-9-30上午09:32:38
 * @version 1.0
 */
public class JpdlModelContainer {
	private Map<String, JpdlModel> jbdlModelMap = new HashMap<String, JpdlModel>();

	/**
	 * 判断是否含有指定流程定义id的JpdlModel
	 * 
	 * @param processDefintionId
	 * @return
	 */
	public boolean hasJpdlModel(String processDefintionId) {
		return jbdlModelMap.containsKey(processDefintionId);
	}

	/**
	 * 获得指定流程定义id的JpdlModel
	 * 
	 * @param processDefintionId
	 * @return
	 */
	public JpdlModel getJpdlModel(String processDefintionId) {
		return jbdlModelMap.get(processDefintionId);
	}
	/**
	 * 放入新的JpdlModel
	 * @param processDefintionId
	 * @param inputStream
	 */
	public void putJpdlModel(String processDefintionId, InputStream inputStream) {
		try {
			JpdlModel jpdlModel = new JpdlModel(inputStream);
			jbdlModelMap.put(processDefintionId, jpdlModel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
