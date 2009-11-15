package test.com.marketsenti.rssfetcher;

import junit.framework.TestCase;

import com.marketsenti.rssfetcher.GoogleReaderFetcher;
import com.marketsenti.rssfetcher.RssFetcherUtils;

public class GoogleReaderFetcherTest extends TestCase
{
 
   public void testGetSubscriptionList()
   {
     GoogleReaderFetcher fetcher = new GoogleReaderFetcher();
     fetcher.initConnection("marketsenti.reader", "marketsenti69!");
     System.out.println(fetcher.GetSubscriptionList());
   }
   
   public void testFetchUnreadList()
   {
     GoogleReaderFetcher fetcher = new GoogleReaderFetcher();
     fetcher.initConnection("marketsenti.reader", "marketsenti69!");
     System.out.println(fetcher.getUnreadFeedAsXmlString("http://rss.cnn.com/rss/money_latest.rss"));
   }
   
   public void testRssFetcherUtils()
   {
     GoogleReaderFetcher fetcher = new GoogleReaderFetcher();
     fetcher.initConnection("marketsenti.reader", "marketsenti69!");
     String xml = fetcher.getUnreadFeedAsXmlString("http://rss.cnn.com/rss/money_latest.rss");
     fetcher.parseRssXml(xml);
   }
}