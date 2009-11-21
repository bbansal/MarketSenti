package test.com.marketsenti.companytagger;

import com.marketsenti.domain.CompanyInfo;
import com.marketsenti.companytagger.*;
import com.marketsenti.storage.*;
import com.marketsenti.Article.*;
import com.marketsenti.companytagger.scorer.*;


import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.io.*;

public class TestSimpleFrequencyTagger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Initialize list of companies and get an iterator.
		
		CompanyInfo companyInfoAMD = new CompanyInfo("AMD");
		List <String> keyWordsAMD= Arrays.asList("AMD", "Advanced Micro Devices", "Hector Ruiz", "Dirk Meyer", "Opteron", "Meyer", "Ruiz", "Barcelona", "Global Foundries", "Globalfoundries", "Hypertransport", "Integrated memory controller");
		companyInfoAMD.setKeywords(keyWordsAMD);
		CompanyInfo companyInfoAAPL = new CompanyInfo("AAPL");
		List <String> keyWordsAAPL= Arrays.asList("AAPL", "Apple Computers", "Apple", "Steve Jobs", "Jobs", "iPhone", "Mac", "iPod", "Macintosh", "Infinite Loop", "Shuffle", "App Store", "iTunes");
		companyInfoAAPL.setKeywords(keyWordsAAPL);
		
		List<CompanyInfo> companyList = Arrays.asList(companyInfoAMD, companyInfoAAPL);
		Iterator<CompanyInfo> iteratorCompanyList = companyList.iterator();
		
		// Read input from a fle and make a string from it
//		String filename = "/home/abhishek/work/testInput/test6.txt";
//		String inputLine = "";
//		String articleString = "";
//		try{
//			BufferedReader reader = new BufferedReader(new FileReader(filename));
//			
//			while((inputLine = reader.readLine()) != null){
//				articleString = articleString+inputLine;
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		String articleString = "Sure, this week's most popular Apple news was still sprinkled with stories about the iPhone, but Office 2008 for Mac, a cost teardown of the third-gen iPod shuffle, and trends in iTunes music sales also made it into the list. Check it out in case you've been avoiding the Mac-o-sphere this week:Devs invited to test push notifications in iPhone OS 3.0: Apple sent out an e-mail to developers opening the doors to live push notification testing. Ars looks at push and tells you how it measures up to notifications from SMS and e-mail.Apple posts third iPhone OS 3.0 beta with minor API changes: The third beta of the upcoming iPhone OS 3.0 is now available to official registered members of Apple's iPhone Developer Program. The biggest change is an improvement to the push notification implementation.AT&T looking to charm Apple, extend iPhone deal to 2011: AT&T's exclusive contract with Apple is set to expire in 2010, meaning there's only one year left until the iPhone can be sold through other carriers. AT&T definitely doesn't want that to happen, and now CEO Randall Stephenson is in talks with Apple to extend that deal to 2011.Fully functional Office 2008 for Mac trial is now available: Microsoft has released a 30-day trial of its latest version of Office for the Mac. The Office 2008 trial is fully functional and should let you get a taste for the updates before you drop some cash on a full version.Apple now offering 20-inch education iMac for $899: Apple has replaced the 17-inch educational iMac with a 20-inch model selling for the same price. The upgraded version is pretty nice for only $899, but it can only be ordered by educational institution buyers.Jobs still keeps watchful eye on Apple even during respite: The indomitable CEO is keeping one hand on the tiller while taking his six-month break from full time duties. Sources suggest he is involved in expected iPhone revisions as well as a tablet-like device.Forrester Research changes tune on iPhone in enterprise: Forrester Research highly criticized the iPhone in 2007 for being enterprise unfriendly, but its latest report suggests that the iPhone can work in the enterprise after all.iSuppli: third-gen iPod shuffle the most profitable yet: iSuppli has performed its ritual teardown on the buttonless iPod shuffle and says that the device only costs around $22 to make. That makes it quite a profitable iPod, though the firm doesn't account for research and development costs when determining these numbers.Some iPhone developers still raking in the cash: Apple gave us a top-20 list for paid and free applications downloaded from the App Store last week. This week, a list of sales numbers has emerged, revealing once again that some iPhone developers are indeed making a pretty penny.Early trends suggest higher iTunes price means lower sales: Songs in the Top 100 that went up in price last week saw drops in chart position on the iTunes Store. However, long-term analysis is needed to find the true effect on revenues.";
		
		// Inititalize Article list
		Article article = new Article();
		System.out.println(articleString);
		article.setText(articleString);
		
		// Instantiate NewsArticletagger
		ArticleCompanyScorer simpleFrequencyScorer = new SimpleFrequencyScorer();
		StorageEngine storage = new InMemoryStorageEngine();
		NewsArticleTagger newsArticleTagger  = new NewsArticleTagger(simpleFrequencyScorer, storage);
		
		newsArticleTagger.TagNewsEntry(article, iteratorCompanyList);
		System.out.println("Companies tagged are"+"");
		article.printCompanyList();
	}

}
