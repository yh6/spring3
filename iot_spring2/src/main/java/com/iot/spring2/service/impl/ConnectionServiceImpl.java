package com.iot.spring2.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.spring2.common.dbcon.DBConnector;
import com.iot.spring2.dao.ConnectionDAO;
import com.iot.spring2.service.ConnectionService;
import com.iot.spring2.vo.ColumnVO;
import com.iot.spring2.vo.ConnectionInfoVO;
import com.iot.spring2.vo.TableVO;

@Service
public class ConnectionServiceImpl implements ConnectionService {
	private static final Logger log = LoggerFactory.getLogger(ConnectionServiceImpl.class);
	@Autowired
	private ConnectionDAO cidao;
	
	@Autowired
	private DBConnector dbc;

	@Override
	public ConnectionInfoVO getConnectionInfo(ConnectionInfoVO ci) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConnectionInfoVO> getConnectionInfoList(ConnectionInfoVO ci) {
		List<ConnectionInfoVO> ciList = cidao.selectConnectionInfoList(ci);
		return ciList;
	}

	@Override
	public void insertConnectionInfo(Map<String, Object> rMap, ConnectionInfoVO ci) {
		int result = cidao.insertConnectionInfo(ci);
		rMap.put("msg", "실패");
		if (result ==1) {
			rMap.put("msg", "성공");
		}

	}

	  public List<Map<String,Object>> getDatabaseList(HttpSession hs,int ciNo)throws Exception {
	      ConnectionInfoVO ci = cidao.selectConnectionInfo(ciNo);
	      dbc.setConnectionInfo(ci); 
	      SqlSession ss = dbc.getSqlSession();
	      hs.setAttribute("sqlSession", ss);
	      log.info("sqlSession=>{}",ss);
	      List<Map<String,Object>> dbList = cidao.selectDatabaseList(ss);
	      int idx = 0;
	      for(Map<String,Object> mDb : dbList) {
	         mDb.put("id", ciNo + "_" + (++idx));
	         mDb.put("text", mDb.get("Database"));
	         mDb.put("items", new Object[] {});
	      }
	      return dbList;
	   }

	   @Override
	   public List<TableVO> getTableList(HttpSession hs, String dbName) {
	      SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
	      return cidao.selectTableList(ss, dbName);
	   }


	@Override
	public List<ColumnVO> getColumnList(HttpSession hs, Map<String, String> map) {
		SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
		return cidao.selectColumnList(ss, map);
	}

	@Override
	public List<ConnectionInfoVO> getConnectionInfoList(String uID) {
		return cidao.selectConnectionInfoList(uID);
	}
	}


