package test.com.marketsenti.newsfetcher;

import junit.framework.TestCase;

import com.marketsenti.newsfetcher.GoogleReaderFetcher;

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
     System.out.println(fetcher.getUnreadFeed("http://rss.cnn.com/rss/money_latest.rss"));
   }
}