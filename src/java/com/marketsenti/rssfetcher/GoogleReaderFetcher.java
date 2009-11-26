package com.marketsenti.rssfetcher;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.google.common.collect.ImmutableMap;
import com.marketsenti.domain.RssArticleEntry;
import com.marketsenti.domain.RssFeedSource;
import com.marketsenti.webutils.HTTPUtils;
import com.marketsenti.webutils.HtmlResponse;

public class GoogleReaderFetcher
{
  private static Logger logger             = Logger.getLogger(GoogleReaderFetcher.class);
  private static String articleElementName = "entry";
  private static String articleTitle       = "title";
  private static String articleSummary     = "summary";
  private static String articlePublishTime = "published";
  private static String articleAuthor      = "author";
  private static String articleSource      = "source";
  private static String articleURL         = "link";
  private static String linkTag            = "href";

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

  public String getUnreadFeedAsXmlString(String rssURL)
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

  public List<RssArticleEntry> parseRssXml(String xml)
  {
    List<RssArticleEntry> rssEntryList = new ArrayList<RssArticleEntry>();
    try
    {
      Document d = new SAXBuilder().build(new StringReader(xml));
      Element rootElement = d.getRootElement();
      System.out.println("atributes:");
      for (Object attrib : rootElement.getAttributes())
      {
        System.out.println("attribute:" + ((Attribute) attrib));
      }

      // ignore all other child elements for now except for actual rss articles
      for (Object childObj : rootElement.getChildren(articleElementName,
                                                     rootElement.getNamespace()))
      {
        Element child = (Element) childObj;
        rssEntryList.add(parseEntry(child, rootElement.getNamespace()));
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException("Error while parsing rssxml:" + xml, e);
    }

    return rssEntryList;
  }

  private RssArticleEntry parseEntry(Element child, Namespace parentNameSpace)
  {
    RssArticleEntry entry = new RssArticleEntry();
    // set source
    entry.setRssSource(getSource(child.getChildText(articleSource, parentNameSpace)));

    entry.setTitle(child.getChildText(articleTitle, parentNameSpace));
    entry.setSummary(child.getChildText(articleSummary, parentNameSpace));
    entry.setAuthor(child.getChildText(articleAuthor, parentNameSpace));
    entry.setPublishedTimestamp(child.getChildText(articlePublishTime, parentNameSpace));

    // article url is as an attribute href.
    entry.setArticleURL(child.getChild(articleURL, parentNameSpace).getAttributeValue(linkTag));

    logger.debug("entry:" + entry);
    return entry;
  }

  private RssFeedSource getSource(String childText)
  {
    // TODO : fix me
    return null;
  }
}
