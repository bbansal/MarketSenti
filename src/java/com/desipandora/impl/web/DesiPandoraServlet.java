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

public class DesiPandoraServlet extends HttpServlet
{
  private static final Logger logger           =
                                                   Logger.getLogger(DesiPandoraServlet.class);
  private static final long   serialVersionUID = 1L;

  private DesiPandoraService  _service;

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
    Enumeration headers = req.getHeaderNames();
    String contextPath = req.getContextPath();
    String queryString = req.getQueryString();
    String sessionId = req.getRequestedSessionId();

    printRequest(cookies, headers, contextPath, queryString, sessionId);

    FileReader reader = new FileReader(new File("/usr/local/apache2/htdocs/index.html"));
    PrintWriter out = resp.getWriter();
    int c;
    while ((c = reader.read()) != -1)
      out.write(c);
  }

  private void printRequest(Cookie[] cookies,
                            Enumeration headers,
                            String contextPath,
                            String queryString,
                            String sessionId)
  {
    StringBuilder builder = new StringBuilder();

    builder.append("(" + "cookies:" + cookies + ")");
    builder.append("(" + "headers:" + headers + ")");
    builder.append("(" + "contextPath:" + contextPath + ")");
    builder.append("(" + "queryString:" + queryString + ")");
    builder.append("(" + "sessionId:" + sessionId + ")");

    logger.info(builder.toString());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException
  {
  }

}
