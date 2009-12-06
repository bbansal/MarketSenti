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
import com.desipandora.sessionInfo.PlayList;
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
	// Play Session specific info
	//private List<VideoEntry> playList;
	//int counterNextSeed ;

	/**
	 * @param storage
	 */
	public DesiPandoraServiceYouTube(StorageEngine storage, String serviceName) {
		super();
		this.storage = storage;
		this.storeName = "DesiPandoraDb"; // temporary. see where to put this.
		storage.createStore(storeName, String.class, SessionEntry.class , null, null);
		this.serviceName = serviceName;
		//this.playList = new ArrayList<VideoEntry>();
		//this.counterNextSeed = 0;
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#createSessionId(java.lang.String)
	 */
	public String createSessionId(String uId) {
		// generate a session Id
		Date date = new Date();
		String sessionId = uId + "_" + date.toString();
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
		SessionEntry sessionEntry = (SessionEntry)(storage.getValues(storeName, sessionId)).next();
		sessionEntry.addFeedbackToSong(songId, feedback);
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#getFirstFewSongs(java.util.List, java.lang.String, int)
	 */
	public List<SongEntry> getFirstFewSongs(List<String> Keywords,
			String sessionId, int numSongs) {
		String queryString = "";
		Iterator<String> iterKeywords = Keywords.iterator();
		List<SongEntry> songEntryList = new ArrayList<SongEntry>();
		while(iterKeywords.hasNext()){
			queryString = queryString + " " + iterKeywords.next();
		}
		YouTubeQuery query = null;
	    try{
	        query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
	        query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
	        query.setFullTextQuery(queryString);
	        query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
	        try{
	          VideoFeed videoFeed = service.query(query, VideoFeed.class);
	          Iterator<SessionEntry> iterStorage = storage.getValues(storeName, sessionId);
	          SessionEntry sessionEntry =  null;
	          if(iterStorage.hasNext()){
	        	  sessionEntry = (SessionEntry) (storage.getValues(storeName, sessionId).next());
	          }
	          else{
	        	  throw new RuntimeException("No Entries exist in storeName "+storeName);
	          }
	          List<VideoEntry> videoEntryList = videoFeed.getEntries();
	          Iterator<VideoEntry> iterVideoEntryList = videoEntryList.iterator();
	          if(iterVideoEntryList.hasNext()){
	        	  VideoEntry videoEntry = iterVideoEntryList.next();
	        	  SongEntry songEntry = new SongEntry(videoEntry.getId(), SongEntryType.YOUTUBE);
	        	  CopyVideoEntryToSongEntry(videoEntry, songEntry); 
	        	  System.out.println("First Title : "+songEntry.getTitle()+" link : "+ songEntry.getRelatedFeedString());	    			
	        	  songEntryList.add(songEntry);
	        	  sessionEntry.addPlayListToSession(new PlayList(songEntryList, 0));
	        	  List<SongEntry> tempSongEntryList = getNextSongs(sessionId, numSongs);
	        	  songEntryList.addAll(tempSongEntryList);
	    	  
	          }
	          else{
	        	  System.out.println("No Videos found");
	          }
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

	   return songEntryList;
	}

	private void CopyVideoEntryToSongEntry(VideoEntry videoEntry,
			SongEntry songEntry) {
		songEntry.setTitle(videoEntry.getTitle().getPlainText());
		songEntry.setRelatedFeedString(videoEntry.getRelatedVideosLink().getHref());
  	  /********************************************************************************
  	   * FIXME : Add some more entries here and do some more search to finally generate 
  	   * a new list of SongEntry. Add all the recommendation code here.
  	   */
	}

	/* (non-Javadoc)
	 * @see com.desipandora.api.DesiPandoraService#getNextSongs(java.lang.String, int)
	 */
	public List<SongEntry> getNextSongs(String sessionId, int numSongs) {
		Iterator<SessionEntry> iterStorage = storage.getValues(storeName, sessionId);
        SessionEntry sessionEntry =  null;
        PlayList sessionPlayList = new PlayList();
        if(iterStorage.hasNext()){
      	  sessionEntry = (SessionEntry) (storage.getValues(storeName, sessionId).next());
      	  sessionPlayList = sessionEntry.getPlayList();
        }
        else{
      	  throw new RuntimeException("No Entries exist in storemane "+storeName);
        }
			
		/* Maintain a list of songIds which will keep track of what order the seongs
		 * have been sent. nextSeed is a pointer to the next song on this list which
		 * has been used as a seed for getRelatedVideos. 
		 * - Label A : get song at next seed and find related videos.
		 * - add these videos to a list(to be returned) as well as the map while discarding duplicates
		 * - if (number retrieved == number requested)
		 *      Update nextSeed pointer
		 *      return this songlist.
		 *   else
		 *      go to A with nextSeed.
		 */          

        List<SongEntry> songEntryList = new ArrayList<SongEntry>();
        List<SongEntry> seedPlayList = new ArrayList<SongEntry>();
        seedPlayList.addAll(sessionPlayList.getPlayList());
        int counterNextSeed = sessionPlayList.getRelatedFeedSeed();
        while ((songEntryList.size() < numSongs) && (seedPlayList.size() > counterNextSeed)){
        	
			SongEntry seedSongEntry = seedPlayList.get(counterNextSeed);
			if(seedSongEntry.getRelatedFeedString() != ""){
				String feedString = seedSongEntry.getRelatedFeedString();
				try{
					VideoFeed relatedVideoFeed = service.getFeed(new URL(feedString), VideoFeed.class);
			        for(VideoEntry loopVideoEntry : relatedVideoFeed.getEntries()){
			        	boolean isDuplicate = FindSimilar(loopVideoEntry);
			        	if(!isDuplicate){
			        		SongEntry songEntry = new SongEntry(loopVideoEntry.getId(), SongEntryType.YOUTUBE);
			        		CopyVideoEntryToSongEntry(loopVideoEntry, songEntry); 
			        		songEntryList.add(songEntry);
			        		seedPlayList.add(songEntry);
			        	}
			        }
			      
				}
				catch (IOException e){
					e.printStackTrace();
				}
				catch (ServiceException e){
					e.printStackTrace();
				}
			}
			counterNextSeed++;
		}
        sessionEntry.addPlayListToSession(new PlayList(songEntryList, counterNextSeed));
        System.out.println("Songs Retrieved "+ songEntryList.size()+", Songs Requested "+numSongs);
		return songEntryList;
	}

	private boolean FindSimilar(VideoEntry loopVideoEntry) {
		// TODO Auto-generated method stub
		return false;
	}

}
