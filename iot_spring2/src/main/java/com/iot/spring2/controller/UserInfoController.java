package com.iot.spring2.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.iot.spring2.service.UserInfoService;
import com.iot.spring2.vo.UserInfoVO;


@Controller
@RequestMapping("/user")
public class UserInfoController {
	@Autowired
	private UserInfoService uis;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserInfoController.class);
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> login(UserInfoVO ui , HttpSession hs){
		Map<String, Object> map = new HashMap<String,Object>();
		if(uis.login(map, ui)) {
			hs.setAttribute("user", map.get("user"));
		}
		return map;
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> join(@RequestBody UserInfoVO ui){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("msg", "가입실패");
		if(uis.join(ui)==1) {
			map.put("msg", ui.getuName()+",님 회원가입에 성공하셨습니다.");
		}
		log.info("insertUI=>{}",ui);
		return map;
	}
}
