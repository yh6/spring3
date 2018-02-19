package com.iot.spring2.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.iot.spring2.vo.ColumnVO;
import com.iot.spring2.vo.ConnectionInfoVO;
import com.iot.spring2.vo.TableVO;

public interface ConnectionDAO {

	public List<ConnectionInfoVO> selectConnectionInfoList(String uID);

	ConnectionInfoVO selectConnectionInfo(int ciNo);

	List<ConnectionInfoVO> selectConnectionInfoList(ConnectionInfoVO ci);

	int insertConnectionInfo(ConnectionInfoVO ci);

	List<Map<String, Object>> selectDatabaseList(SqlSession ss) throws Exception;

	List<TableVO> selectTableList(SqlSession ss, String dbName);

	List<ColumnVO> selectColumnList(SqlSession ss, Map<String, String> map);

	int useDataBase(String dbName, SqlSession ss);
}
