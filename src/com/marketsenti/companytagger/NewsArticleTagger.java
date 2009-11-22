/**
 * 
 */
package com.marketsenti.companytagger;

import com.marketsenti.companytagger.scorer.*;
import com.marketsenti.storage.*;
import com.marketsenti.storage.serializer.StringSerializer;
import com.marketsenti.Article.*;
import com.marketsenti.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Gets a new article and based on a list of Objects (Company), tags them with a
 * particluar company and stores it in a local database.
 * 
 * @author abhishek
 * 
 */
public class NewsArticleTagger
{
  private List<CompanyInfo>     companyList     = new ArrayList<CompanyInfo>();
  private Iterator<CompanyInfo> companyListIter = companyList.iterator();
  private ArticleCompanyScorer  scorer;
  private StorageEngine         storage;
  private long                  lastTaggedTime;

  public NewsArticleTagger(ArticleCompanyScorer scorer, StorageEngine storage)
  {
    this.scorer = scorer;
    this.storage = storage;
    StringSerializer serializer = new StringSerializer();
    storage.createStore("ArticleTagger",
                        String.class,
                        String.class,
                        serializer,
                        serializer);
  }

  /*
   * This function assumes that raw articles from text extractor are all stores in a
   * storeName with Key = String (TimeStamp) and Value = Article
   */
  public void TagArticlesSinceTime(String sourceStoreName, long timestamp)
  {
    Iterator<StoreEntry<String, Article>> sourceIter = storage.scanStore(sourceStoreName);

    while (sourceIter.hasNext())
    {
      Article article = sourceIter.next().getValue();
      TagNewsEntry(article, companyListIter);

    }
  }

  public void TagNewsEntry(Article article, Iterator<CompanyInfo> companyIter)
  {
    double topScore = 0.0;
    CompanyInfo company = null;
    CompanyInfo topCompany = null;

    // while(companyIter.hasNext()){
    // System.out.println("Inside TagNewsEntry"+companyIter.next().getName());
    // }
    //		
    while (companyIter.hasNext())
    {
      company = companyIter.next();
      double companyScore = scorer.score(article, company);
      if (companyScore > topScore)
      {
        topScore = companyScore;
        topCompany = company;
      }
    }
    article.addCompany(topCompany);

  }

  // public void

}
