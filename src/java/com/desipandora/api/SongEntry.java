package com.desipandora.api;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  private String Description;
  private List<String> keyWords;
  float                youTubeRating;
  private URL          url;
  private String          relatedFeedString;
  private String       youtubeId;
  private SongEntryType type;
  private Set<String> titleWordsSet;

  public String getDescription() {
	return Description;
}

public void setDescription(String description) {
	Description = description;
}

public List<String> getKeyWords() {
	return keyWords;
}

public void setKeyWords(List<String> keyWords) {
	this.keyWords = keyWords;
}

public float getYouTubeRating() {
	return youTubeRating;
}

public void setYouTubeRating(float youTubeRating) {
	this.youTubeRating = youTubeRating;
}

public SongEntry(String songID, SongEntryType type)
  {
    setSongId(songID);
    setType(type);
    titleWordsSet = new HashSet<String>();
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
    String trimmedTitle = this.title.replaceAll("[^A-Za-z_ ]", "");
    String[] splittedTitle = trimmedTitle.split("[\\s|_]+");
    
    for(int i = 0; i<splittedTitle.length; i++){
    	titleWordsSet.add(splittedTitle[i]);
    }
  }	

  public Set<String> getTitleWordsSet(){
	  return titleWordsSet;
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
