package com.marketsenti.webutils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

public class HTTPUtils
{
  private static Logger logger = Logger.getLogger(HTTPUtils.class);

  public static HtmlResponse HttpGetResponseAsString(String urlString,
                                                     Map<String, String> props) throws ClientProtocolException,
      IOException
  {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoInput(true);
    conn.setDoOutput(true);

    conn.setRequestMethod("GET");
    // set default properties.
    setDefaultHttpHeaders(conn);

    // override with properties.
    overrideProperties(conn, props);

    conn.connect();

    Map<String, String> headers = readHeaders(conn);
    String responseString = readResponseString(conn);

    HtmlResponse response = new HtmlResponse(responseString, headers);
    // check status
    if (!checkResponseCode(conn.getResponseCode()))
    {
      throw new RuntimeException("Http Post failed with response message:"
          + conn.getResponseMessage() + " response:");
    }
    logger.debug("Get (" + urlString + ") responded with " + response);
    return response;
  }

  private static void setDefaultHttpHeaders(URLConnection conn)
  {
    conn.setRequestProperty("Accept",
                            "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*");
    conn.setRequestProperty("Accept-Language", "en-us");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
    conn.setRequestProperty("User-Agent", "Mozilla/4.0");
    conn.setRequestProperty("Cache-Control", "no-cache");
  }

  public static HtmlResponse HttpPostResponseAsString(String urlString,
                                                      String data,
                                                      Map<String, String> props) throws ClientProtocolException,
      IOException
  {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoInput(true);
    conn.setDoOutput(true);

    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Length", "" + data.length());

    // set default properties.
    setDefaultHttpHeaders(conn);

    // override with properties.
    overrideProperties(conn, props);

    conn.connect();

    // write data
    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
    dos.writeBytes(data);
    dos.flush();
    dos.close();

    Map<String, String> headers = readHeaders(conn);
    String responseString = readResponseString(conn);

    HtmlResponse response = new HtmlResponse(responseString, headers);
    // check status
    if (!checkResponseCode(conn.getResponseCode()))
    {
      throw new RuntimeException("Http Post failed with response message:"
          + conn.getResponseMessage() + " response:");
    }
    
    return response;
  }

  private static void overrideProperties(HttpURLConnection conn, Map<String, String> props)
  {
    if (null != props)
    {
      for (Entry<String, String> entry : props.entrySet())
      {
        conn.setRequestProperty(entry.getKey(), entry.getValue());
      }
    }
  }

  private static boolean checkResponseCode(int responseCode)
  {
    return responseCode == HttpURLConnection.HTTP_OK;
  }

  private static String readResponseString(HttpURLConnection conn) throws IOException
  {
    StringBuffer buffer = new StringBuffer();

    DataInputStream dis = new DataInputStream(conn.getInputStream());
    String s = null;
    while ((s = dis.readLine()) != null)
    {
      buffer.append(s);
      buffer.append("\n");
    }

    return buffer.toString();
  }

  private static Map<String, String> readHeaders(URLConnection conn)
  {
    Map<String, String> map = new HashMap<String, String>();
    int i = 1;
    String hdrKey = null;
    while ((hdrKey = conn.getHeaderFieldKey(i)) != null)
    {
      map.put(hdrKey, conn.getHeaderField(i));
      i++;
    }
    return map;
  }

}
