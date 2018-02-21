package com.iot.spring2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.spring2.service.ConnectionService;
import com.iot.spring2.vo.ColumnVO;
import com.iot.spring2.vo.ConnectionInfoVO;
import com.iot.spring2.vo.TableVO;
import com.iot.spring2.vo.UserInfoVO;

@Controller
@RequestMapping("/connection")
public class ConnectionController {
	private static final Logger log = LoggerFactory.getLogger(ConnectionController.class);

	@Autowired
	private ConnectionService cis;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertConnectionInfo(@Valid ConnectionInfoVO ci, Map<String, Object> map,HttpSession hs) {
		log.info("ci=>{}", ci);
		
		cis.insertConnectionInfo(map, ci,hs);
		return map;
	}

	@RequestMapping(value = "/db_list/{ciNo}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDatabaseList(@PathVariable("ciNo") int ciNo, Map<String, Object> map,
			HttpSession hs) {
		List<Map<String, Object>> dbList;
		try {
			dbList = cis.getDatabaseList(hs, ciNo);
			map.put("list", dbList);
			map.put("parentId", ciNo);
		} catch (Exception e) {
			map.put("error", e.getMessage());
			log.error("db connection error =>{}", e);
		}
		return map;
	}

	@RequestMapping(value = "/tables/{dbName}/{parentId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTableList(@PathVariable("dbName") String dbName,
			@PathVariable("parentId") String parentId, HttpSession hs, Map<String, Object> map) {
		int useOk = cis.useDataBase(dbName, hs);
		log.info("useOk={}", useOk);
		log.info("dbName={}", dbName);
		List<TableVO> tableList = cis.getTableList(hs, dbName);
		map.put("list", tableList);
		map.put("parentId", parentId);
		return map;
	}

	@RequestMapping(value = "/columns", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getColumnList(Map<String, Object> map) {
		/*
		 * List<Map<String,Object>> dbList = cs.getDatabaseList(); map.put("dbList",
		 * dbList); cis.getColumnList(hs,map)
		 */
		return map;
	}

	@RequestMapping(value = "/columns/{dbName}/{tableName}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getColumnList(@PathVariable("dbName") String dbName,
			@PathVariable("tableName") String tableName, Map<String, Object> map, HttpSession hs) {
		Map<String, String> pMap = new HashMap<String, String>();
		pMap.put("dbName", dbName);
		pMap.put("tableName", tableName);
		List<ColumnVO> cList = cis.getColumnList(hs, pMap);
		List<Map<String, Object>> TDList = cis.getTDList(tableName, hs);
		map.put("cList", cList);
		map.put("TDList", TDList);
		return map;
	}

	@RequestMapping(value = "/list")
	public @ResponseBody Map<String, Object> getConnectInfoList(Map<String, Object> map, HttpSession hs) {
		UserInfoVO ui = new UserInfoVO();
		if (hs.getAttribute("user") != null) {
			ui = (UserInfoVO) hs.getAttribute("user");
		} else {
			ui.setuID("red");
		}
		List<ConnectionInfoVO> ciList = cis.getConnectionInfoList(ui.getuID());
		map.put("list", ciList);
		return map;
	}
}
