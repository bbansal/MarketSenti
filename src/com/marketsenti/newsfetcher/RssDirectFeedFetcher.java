package com.marketsenti.newsfetcher;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.marketsenti.webutils.HTTPUtils;
import com.marketsenti.webutils.HtmlResponse;

public class RssDirectFeedFetcher
{
  private static Logger logger = Logger.getLogger(RssDirectFeedFetcher.class);

  public HtmlResponse fetchDirectRssFeed(String rssURL, String lastModifiedDate)
  {
    try
    {
      HtmlResponse response = HTTPUtils.HttpGetResponseAsString(rssURL, ImmutableMap.of("If-Modified-Since", lastModifiedDate));
      logger.debug("rssFeedDirectUrl:" + rssURL + " \n response:\n" + response);
      return response;
    }
    catch (Exception e)
    {
      logger.error("fetchRssFeed failed for rssURL:" + rssURL, e);
      return null;
    }
  }

}
