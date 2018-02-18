package com.iot.spring2.controller;

import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);
	
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
		map.put("biz", false);
		int result = uis.join(ui);
		if(result==1) {
			map.put("msg", "회원가입 성공 ");
			map.put("biz", true);
		}else if(result==2) {
			map.put("msg", "아이디 중복 확인 요청");
		}
		return map;
	}
	@RequestMapping(value="/check/{uID}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> join2(@PathVariable String uID){
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("insertUI=>{}",uID);
		map.put("msg", "아이디 중복 임마~");
		map.put("biz", false);
		if(uis.checkUserId(uID)==0) {
			map.put("msg", "없는 아이디");
			map.put("biz", true);
		}
		return map;
	}
}
