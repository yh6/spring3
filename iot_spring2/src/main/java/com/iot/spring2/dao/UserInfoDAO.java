package com.iot.spring2.dao;

import com.iot.spring2.vo.UserInfoVO;

public interface UserInfoDAO {

	public UserInfoVO selectUserInfo(UserInfoVO ui);
	
}
