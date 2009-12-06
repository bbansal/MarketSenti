package com.desipandora.impl.web;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.desipandora.api.DesiPandoraService;
import com.desipandora.youTube.DesiPandoraServiceYouTube;

public class DesiPandoraServlet extends HttpServlet {
	private static final Logger logger = Logger
			.getLogger(DesiPandoraServlet.class);
	private static final long serialVersionUID = 1L;

	private DesiPandoraService _service;

	@Override
	public void init(ServletConfig config) throws ServletException {
		_service = new DesiPandoraServiceYouTube();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		Enumeration headers = req.getHeaderNames();
		String contextPath = req.getContextPath();
		String queryString = req.getQueryString();
		String sessionId = req.getRequestedSessionId();
		String pathInfo = req.getPathInfo();

		printRequest(cookies, headers, contextPath, queryString, sessionId,
				pathInfo);
		resp.getWriter().write(printRequest(cookies, headers, contextPath, queryString, sessionId, pathInfo));
		resp.getWriter().flush();
	}

	private String printRequest(Cookie[] cookies, Enumeration headers,
			String contextPath, String queryString, String sessionId,
			String pathInfo) {
		StringBuilder builder = new StringBuilder();

		builder.append("(" + "cookies:" + cookies + ")");
		builder.append("(" + "headers:" + headers + ")");
		builder.append("(" + "contextPath:" + contextPath + ")");
		builder.append("(" + "queryString:" + queryString + ")");
		builder.append("(" + "sessionId:" + sessionId + ")");
		builder.append("(" + "pathInfo:" + pathInfo + ")");

		logger.info(builder.toString());
		return builder.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

}
