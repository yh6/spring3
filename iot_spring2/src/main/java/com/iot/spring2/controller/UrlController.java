package com.iot.spring2.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/path")
public class UrlController {
	private static final Logger log = LoggerFactory.getLogger(UrlController.class);
	
	private String getUrl(String url, String rootPath) {
		return url.replace(rootPath + "/path", "");
	}
	@RequestMapping("/**")
	public ModelAndView goJsp(HttpServletRequest req, ModelAndView mav) {
		String url = req.getRequestURI();
		String rootPath = req.getContextPath();
		url = getUrl(url,rootPath);
		log.info("path=>{}",url);
		mav.setViewName(url); 
		return mav;
		
	}

}
