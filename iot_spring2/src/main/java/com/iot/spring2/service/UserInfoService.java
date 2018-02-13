package com.iot.spring2.service;

import java.util.Map;

import com.iot.spring2.vo.UserInfoVO;

public interface UserInfoService {

	public boolean login(Map<String, Object> rMap, UserInfoVO ui);
}
