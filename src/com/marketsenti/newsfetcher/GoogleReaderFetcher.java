package com.marketsenti.newsfetcher;

import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.marketsenti.webutils.HTTPUtils;
import com.marketsenti.webutils.HtmlResponse;

public class GoogleReaderFetcher
{
  private static Logger logger = Logger.getLogger(GoogleReaderFetcher.class);
  private String        sid;
  private String        token;
  private String        username;

  public void initConnection(String username, String password)
  {
    this.username = username;
    // step 1: obtain SID
    String loginurl = "https://www.google.com/accounts/ClientLogin";
    NameValuePair[] data =
        { new BasicNameValuePair("Email", username),
            new BasicNameValuePair("Passwd", password) };
    String dataString = URLEncodedUtils.format(Arrays.asList(data), "UTF-8");
    try
    {
      HtmlResponse response =
          HTTPUtils.HttpPostResponseAsString(loginurl,
                                             dataString,
                                             ImmutableMap.of("Content-Type",
                                                             "application/x-www-form-urlencoded"));
      sid = response.getResponseString();
      StringBuilder output2 = new StringBuilder(sid);
      sid = output2.substring(4, sid.indexOf("LSID"));
      sid = sid.trim();
    }
    catch (Exception e)
    {
      logger.error("Failed to get login", e);
    }

    // We now have SID, step 2 is to obtain the token string
    String tokenURL = "http://www.google.com/reader/api/0/token";
    try
    {
      HtmlResponse response =
          HTTPUtils.HttpGetResponseAsString(tokenURL, ImmutableMap.of("Cookie", "SID="
              + sid));
      token = response.getResponseString();
    }
    catch (Exception e)
    {
      logger.error("Failed to get token", e);
    }
  }

  public String GetSubscriptionList()
  {
    String subscriptionListURL =
        "http://www.google.com/reader/api/0/subscription/list?output=xml";

    try
    {
      HtmlResponse response =
          HTTPUtils.HttpGetResponseAsString(subscriptionListURL,
                                            ImmutableMap.of("Cookie", "SID=" + sid));
      return response.getResponseString();
    }
    catch (Exception e)
    {
      logger.error("Failed to get subscription list", e);
    }

    return null;
  }

  public String getUnreadFeed(String rssURL)
  {
    String googleReaderBase = "http://www.google.com/reader/atom/feed/";
    rssURL = URLEncoder.encode(rssURL);
    try
    {
      HtmlResponse response =
          HTTPUtils.HttpGetResponseAsString(googleReaderBase + rssURL,
                                            ImmutableMap.of("Cookie", "SID=" + sid));
      return response.getResponseString();
    }
    catch (Exception e)
    {
      logger.error("Failed to fetch Unread feed list", e);
    }

    return null;
  }
}
