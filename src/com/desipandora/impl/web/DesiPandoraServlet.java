package com.desipandora.impl.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.desipandora.api.DesiPandoraService;


public class DesiPandoraServlet extends HttpServlet
{
  private static final Logger logger = Logger.getLogger(DesiPandoraServlet.class);
  private static final long serialVersionUID = 1L;
  
  private DesiPandoraService _service ;

  @Override
  public void init(ServletConfig config) throws ServletException
  {
    // TODO : fix me
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException
  {
    Cookie[] cookies = req.getCookies();
    Enumeration headers = req.getHeaderNames() ;
    String contextPath = req.getContextPath();
    String queryString = req.getQueryString();
    String sessionId = req.getRequestedSessionId();
    
    printRequest(cookies, headers, contextPath, queryString, sessionId);
  }
  
  private void printRequest(Cookie[] cookies,
                            Enumeration headers,
                            String contextPath,
                            String queryString,
                            String sessionId)
  {
    StringBuilder builder = new StringBuilder();
    
    builder.append("cookies:" + cookies + "\n");
    builder.append("headers:" + headers + "\n");
    builder.append("contextPath:" + contextPath + "\n");
    builder.append("queryString:" + queryString + "\n");
    builder.append("sessionId:" + sessionId + "\n");

    logger.info(builder.toString());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException
  {
  }

  
}
