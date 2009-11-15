package com.marketsenti.domain;

import sun.security.action.GetLongAction;

public class RssArticleEntry
{
  private RssFeedSource rssSource;
  private String title;
  private String author;
  private String   publishedTimeStamp;
  private String articleURL;
  private String summary;

  public void setRssSource(RssFeedSource rssSource)
  {
    this.rssSource = rssSource;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public void setAuthor(String author)
  {
    this.author = author;
  }

  public void setPublishedTimestamp(String timestamp)
  {
    this.publishedTimeStamp = timestamp;
  }

  public void setArticleURL(String articleURL)
  {
    this.articleURL = articleURL;
  }

  public void setSummary(String summary)
  {
    this.summary = summary;
  }

  public RssFeedSource getRssSource()
  {
    return rssSource;
  }

  public String getTitle()
  {
    return title;
  }

  public String getAuthor()
  {
    return author;
  }

  public String getPublishedTimestamp()
  {
    return publishedTimeStamp;
  }

  public String getArticleURL()
  {
    return articleURL;
  }

  public String getSummary()
  {
    return summary;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("title:(");
    builder.append(getTitle() + ") ");
    builder.append("summary:(");
    builder.append(getSummary() + ") ");
    builder.append("author:(");
    builder.append(getAuthor() + ") ");
    builder.append("published:(");
    builder.append(getPublishedTimestamp() + ") ");
    builder.append("link:(");
    builder.append(getArticleURL() + ") ");
    builder.append("source:(");
    builder.append(getRssSource() + ") ");
    
    return builder.toString(); 
  }
}
