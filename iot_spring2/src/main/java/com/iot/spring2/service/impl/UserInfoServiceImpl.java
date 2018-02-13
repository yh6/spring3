package com.iot.spring2.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.spring2.dao.UserInfoDAO;
import com.iot.spring2.service.UserInfoService;
import com.iot.spring2.vo.UserInfoVO;

@Service
public class UserInfoServiceImpl implements UserInfoService{
	@Autowired
	private UserInfoDAO uidao;
	
	@Override
	public boolean login(Map<String, Object> rMap, UserInfoVO ui) {
		ui = uidao.selectUserInfo(ui);
		rMap.put("msg", "아이디 비밀번호 확인해주세요");
		rMap.put("biz", false);
		if(ui!=null) {
			rMap.put("msg", ui.getuName() + ".님 로그인의 성공하셧습니다.");
			rMap.put("biz", true);
			return true;
		}
		return false;
	}

}
