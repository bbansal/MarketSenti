/**
 * 
 */
package com.desipandora.youTube;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.desipandora.api.DesiPandoraService;
import com.desipandora.api.SongEntry;
import com.desipandora.api.SongEntry.SongEntryType;
import com.desipandora.sessionInfo.SessionEntry;
import com.desipandora.sessionInfo.SongFeedbackEntry;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;
import com.marketsenti.storage.StorageEngine;

/**
 * @author abhishek
 *
 */
public class DesiPandoraServiceYouTube implements DesiPandoraService {

	private StorageEngine storage;
	private String storeName;
	private YouTubeService service;
	private String serviceName;
	/**
	 * @param storage
	 */
	public DesiPandoraServiceYouTube(StorageEngine storage, String serviceName) {
		super();
		this.storage = storage;
		this.storeName = "DesiPandoraDb"; // temporary. see where to put this.
		storage.createStore(storeName, String.class, SessionEntry.class , null, null);
		this.serviceName = serviceName;
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#createSessionId(java.lang.String)
	 */
	public String createSessionId(String uId) {
		// TODO Auto-generated method stub
		// generate a session Id
		Date date = new Date();
		String sessionId = date.toString();
		List<SessionEntry> arrayList = new ArrayList<SessionEntry>(); 
		arrayList.add(new SessionEntry(uId));
		System.out.println("Session id is "+sessionId);
		storage.<String, SessionEntry>putValue(storeName, sessionId, arrayList.iterator(), date.getTime());
		service = new YouTubeService(serviceName);
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#feedback(java.lang.String, java.lang.String, double)
	 */
	public void feedback(String sessionId, String songId, double feedback) {
		// TODO Auto-generated method stub
		SessionEntry sessionEntry = (SessionEntry)(storage.getValues(storeName, sessionId)).next();
		sessionEntry.addFeedbackToSong(songId, feedback);
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#getFirstFewSongs(java.util.List, java.lang.String, int)
	 */
	public List<SongEntry> getFirstFewSongs(List<String> Keywords,
			String sessionId, int numSongs) {
		// TODO Auto-generated method stub
		String queryString = "";
		Iterator<String> iterKeywords = Keywords.iterator();
		List<SongEntry> songEntryList = new ArrayList<SongEntry>();
		while(iterKeywords.hasNext()){
			queryString = queryString + iterKeywords.next();
		}
		YouTubeQuery query = null;
	    try{
	    	// FIXME :  Might need some changes based on implementations
	        query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
	        query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
	        query.setFullTextQuery(queryString);
	        query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
	        try{
	          VideoFeed videoFeed = service.query(query, VideoFeed.class);
	          //FIXME : do stuff here
	          Iterator<SessionEntry> iterStorage = storage.getValues(storeName, sessionId);
	          SessionEntry sessionEntry =  null;
	          if(iterStorage.hasNext()){
	        	  sessionEntry = (SessionEntry) (storage.getValues(storeName, sessionId).next());
	          }
	          else{
	        	  System.out.println("No Entries exist in storaname "+storeName);
	          }
	          for(VideoEntry videoEntry : videoFeed.getEntries()){
	        	  //FIXME :  Getting IDs here might need to be revisited at some point.
	        	  SongEntry songEntry = new SongEntry(videoEntry.getId(), SongEntryType.YOUTUBE);
	        	  songEntry.setTitle(videoEntry.getTitle().getPlainText());
	        	  // 
	        	  /********************************************************************************
	        	   * FIXME : Add some more entries here and do some more search to finally generate 
	        	   * a new list of SongEntry. Add all the recommendation code here.
	        	   */
	        	  sessionEntry.addSongToMap(songEntry.getSongId(), new SongFeedbackEntry(songEntry, 0.0));
	        	  songEntryList.add(songEntry);
	        	  
	          }
	          return songEntryList;
	        }
	        catch(IOException e){
	          e.printStackTrace();
	        }
	        catch (ServiceException e){
	          e.printStackTrace();
	        }
	   
	      }
	      catch(MalformedURLException e){
	        System.out.println("Malformed URL");
	        e.printStackTrace();
	      }

		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#getNextSongs(java.lang.String, int)
	 */
	public List<SongEntry> getNextSongs(String sessionId, int numSongs) {
		// TODO Auto-generated method stub
		return null;
	}

}
