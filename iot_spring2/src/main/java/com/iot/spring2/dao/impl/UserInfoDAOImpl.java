package com.iot.spring2.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iot.spring2.dao.UserInfoDAO;
import com.iot.spring2.vo.UserInfoVO;

@Repository
public class UserInfoDAOImpl implements UserInfoDAO{
	@Autowired
	private SqlSessionFactory ssf;
	@Override
	public UserInfoVO selectUserInfo(UserInfoVO ui) {
		SqlSession ss = ssf.openSession();
		ui = ss.selectOne("user_info.selectUserInfo",ui);	
		ss.close();
		return ui;
	}

}
