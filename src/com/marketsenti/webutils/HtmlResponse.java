package com.marketsenti.webutils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;

public class HtmlResponse
{
  public enum HtmlResponseFormat
  {
    STRING, BYTE, XML, JSON
  }

  private String              responseString;
  private Map<String, String> headers;
  private byte[]              bytes;
  private HtmlResponseFormat  type;

  public HtmlResponse(String responseString, Map<String, String> headers)
  {
    this.responseString = responseString;
    this.headers = headers;
    this.type = HtmlResponseFormat.STRING;
  }

  public String getResponseString()
  {
    return responseString;
  }

  public void setResponseString(String response)
  {
    this.responseString = response;
  }

  public Map<String, String> getHeaders()
  {
    return headers;
  }

  public void setHeaders(Map<String, String> headers)
  {
    this.headers = headers;
  }

  public byte[] getBytes()
  {
    return bytes;
  }

  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }

  public HtmlResponseFormat getType()
  {
    return type;
  }

  public void setType(HtmlResponseFormat type)
  {
    this.type = type;
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("headers:(");
    for (Entry<String,String> header : headers.entrySet())
    {
      buffer.append(" (" + header.getKey() + "," + header.getValue() + ") ");
    }
    buffer.append(")\n");

    switch (this.type)
    {
    case STRING:
      buffer.append(this.responseString);
      break;
    case BYTE:
      buffer.append("data(" + bytes.length + ")");
      break;
    }

    return buffer.toString();
  }
}
