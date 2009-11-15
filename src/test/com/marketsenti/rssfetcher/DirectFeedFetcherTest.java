package test.com.marketsenti.rssfetcher;

import java.util.Date;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.impl.cookie.DateUtils;

import com.marketsenti.rssfetcher.RssDirectFeedFetcher;
import com.marketsenti.webutils.HtmlResponse;

import junit.framework.TestCase;

public class DirectFeedFetcherTest extends TestCase
{
  private static String  cnnUrl = "http://rss.cnn.com/rss/money_topstories.rss";
  private static String yahooStocksRss = "http://rss.news.yahoo.com/rss/stocks";
 
   public void testFetchDirectRss()
   {
     RssDirectFeedFetcher feedFetcher = new RssDirectFeedFetcher();
     String lastModifiedDate = DateUtils.formatDate(new Date(System.currentTimeMillis()));
     HtmlResponse response = feedFetcher.fetchDirectRssFeed(yahooStocksRss, lastModifiedDate);
     assertNotNull(response);   
     
     // try fetching with the correct modified date.
     lastModifiedDate = getHeaderValue(response, "Date");
     HtmlResponse response2 = feedFetcher.fetchDirectRssFeed(yahooStocksRss, lastModifiedDate);
     assertNotNull(response2); 
     
     assertNotSame(response.getResponseString(), response2.getResponseString());
   }


  private String getHeaderValue(HtmlResponse response, String name)
  {
    for (Entry<String,String> header: response.getHeaders().entrySet())
    {
      if (header.getKey().equals(name))
      {
        return header.getValue(); 
      }
    }
    
    return null;
  }
}