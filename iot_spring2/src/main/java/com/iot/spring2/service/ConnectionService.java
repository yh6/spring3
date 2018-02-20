package com.iot.spring2.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import com.iot.spring2.vo.ColumnVO;
import com.iot.spring2.vo.ConnectionInfoVO;
import com.iot.spring2.vo.TableVO;

public interface ConnectionService {

	public List<ConnectionInfoVO> getConnectionInfoList(String uID);
	ConnectionInfoVO getConnectionInfo(ConnectionInfoVO ci);
	List<ConnectionInfoVO> getConnectionInfoList(ConnectionInfoVO ci);
	void insertConnectionInfo(Map<String, Object> rMap, ConnectionInfoVO ci);
	List<TableVO> getTableList(HttpSession hs, String dbNameS);
	List<Map<String, Object>> getDatabaseList(HttpSession hs, int ciNo) throws Exception;
	List<ColumnVO> getColumnList(HttpSession hs, Map<String, String> map);
	public int useDataBase(String dbName, HttpSession hs);	
	public List<Map<String, Object>> getTDList(String tableName, HttpSession hs);
}
