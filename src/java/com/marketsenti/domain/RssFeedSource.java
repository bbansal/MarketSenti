package com.marketsenti.domain;

public class RssFeedSource
{
  private String rssSourceURL;
  private String summary;
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
  public void setSummary(String summary)
  {
    this.summary = summary;
  }
  public String getSummary()
  {
    return summary;
  }
 
}