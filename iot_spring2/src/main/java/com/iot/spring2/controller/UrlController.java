package com.iot.spring2.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller

public class UrlController {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(UrlController.class);
	
	private String getUrl(String url, String rootPath) {
		return url.replace(rootPath + "/path", "");
	}
	@RequestMapping("/path/**")
	public ModelAndView goJsp(HttpServletRequest req, ModelAndView mav) {
		String url = req.getRequestURI();
		String rootPath = req.getContextPath();
		url = getUrl(url,rootPath);
		log.info("path=>{}",url);
		mav.setViewName(url); 
		return mav;
		
	}
	@RequestMapping("/")
	public ModelAndView goIndex(ModelAndView mav) {
		mav.setViewName("index");
		return mav;
	}


}
