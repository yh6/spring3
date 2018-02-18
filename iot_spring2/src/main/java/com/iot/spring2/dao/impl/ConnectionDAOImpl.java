package com.iot.spring2.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iot.spring2.dao.ConnectionDAO;
import com.iot.spring2.vo.ColumnVO;
import com.iot.spring2.vo.ConnectionInfoVO;
import com.iot.spring2.vo.TableVO;


@Repository
public class ConnectionDAOImpl implements ConnectionDAO {
	@Autowired
	private SqlSessionFactory ssf;

	@Override
	public ConnectionInfoVO selectConnectionInfo(int ciNo) {
		SqlSession ss = ssf.openSession();
		ConnectionInfoVO ci = ss.selectOne("connection.selectConnectionInfoWithCiNo", ciNo);
		ss.close();
		return ci;
	}

	@Override
	public List<ConnectionInfoVO> selectConnectionInfoList(String uID) {
		SqlSession ss = ssf.openSession();
		List<ConnectionInfoVO> ciList = ss.selectList("connection.selectConnectionInfo", uID);
		return ciList;
	}

	@Override
	public int insertConnectionInfo(ConnectionInfoVO ci) {
		int result = 0;
		final SqlSession ss = ssf.openSession();
		result = ss.insert("connection.insertConnectionInfo", ci);
		ss.close();
		return result;

	}

	@Override
	public List<Map<String, Object>> selectDatabaseList(SqlSession ss) throws Exception {
		return ss.selectList("connection.selectDatabase");

	}

	@Override
	public List<TableVO> selectTableList(SqlSession ss, String dbName) {
		List<TableVO> result = null;
		result = ss.selectList("connection.selectTable", dbName);
		return result;
	}

	@Override
	public List<ConnectionInfoVO> selectConnectionInfoList(ConnectionInfoVO ci) {
		List<ConnectionInfoVO> result = null;
		final SqlSession ss = ssf.openSession();
		result = ss.selectList("connection.selectConnectionInfo", ci);
		ss.close();
		return result;
	}

	@Override
	public List<ColumnVO> selectColumnList(SqlSession ss, Map<String, String> map) {
		return ss.selectList("connection.selectColumn", map);
	}

}
