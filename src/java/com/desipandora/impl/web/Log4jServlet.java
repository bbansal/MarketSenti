package com.desipandora.impl.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Log4jServlet extends HttpServlet
{

  private static final long serialVersionUID = 1;

  @Override
  public void init() throws ServletException
  {
    String prefix = getServletContext().getRealPath("/");
    String file = getInitParameter("log4j-file");
    // if the log4j-init-file is not set, then no point in trying
    if (file != null)
    {
      PropertyConfigurator.configure(prefix + file);
      System.out.println("file:" + prefix + file);
    }
    getServletContext().log("logging to: " + prefix + file);
  }

}
