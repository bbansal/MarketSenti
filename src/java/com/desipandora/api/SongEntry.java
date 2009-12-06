package com.desipandora.api;

import java.net.URL;
import java.util.List;

public class SongEntry
{
  public enum SongEntryType
  {
    YOUTUBE, MP3
  }

  private String       songId;
  private String       title;
  private List<String> artists;
  private String       albums;
  private URL          url;
  private String          relatedFeedString;
  private String       youtubeId;
  private SongEntryType type;

  public SongEntry(String songID, SongEntryType type)
  {
    setSongId(songID);
    setType(type);
  }
  
  public String getRelatedFeedString() {
	return relatedFeedString;
}

public void setRelatedFeedString (String relatedFeedString) {
	this.relatedFeedString = relatedFeedString ;
}

public String getSongId()
  {
    return songId;
  }

  public void setSongId(String songId)
  {
    this.songId = songId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public List<String> getArtists()
  {
    return artists;
  }

  public void setArtists(List<String> artists)
  {
    this.artists = artists;
  }

  public String getAlbums()
  {
    return albums;
  }

  public void setAlbums(String albums)
  {
    this.albums = albums;
  }

  public URL getUrl()
  {
    return url;
  }

  public void setUrl(URL url)
  {
    this.url = url;
  }

  public String getYoutubeId()
  {
    return youtubeId;
  }

  public void setYoutubeId(String youtubeId)
  {
    this.youtubeId = youtubeId;
  }
  public void setType(SongEntryType type)
  {
    this.type = type;
  }
  public SongEntryType getType()
  {
    return type;
  }

}
