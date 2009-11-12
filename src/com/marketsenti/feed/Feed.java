package com.marketsenti.feed;

public class Feed
{
  private String rssSourceURL;
  private String lastModifiedDate;
  
  
  public String getRssSourceURL()
  {
    return rssSourceURL;
  }
  public void setRssSourceURL(String rssSourceURL)
  {
    this.rssSourceURL = rssSourceURL;
  }
  public String getLastModifiedDate()
  {
    return lastModifiedDate;
  }
  public void setLastModifiedDate(String lastModifiedDate)
  {
    this.lastModifiedDate = lastModifiedDate;
  }
 
}