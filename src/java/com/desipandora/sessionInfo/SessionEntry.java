/**
 * 
 */
package com.desipandora.sessionInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author abhishek
 *
 */
public class SessionEntry {
	String userId;
	long timeStamp;
	Map<String, SongFeedbackEntry> songFeedbackEntryMap;
	
	public SessionEntry(String userId) {
		super();
		this.userId = userId;
		this.songFeedbackEntryMap = new HashMap<String, SongFeedbackEntry>();
		this.timeStamp = (new Date()).getTime();
	}

	public void addSongToMap(String songId, SongFeedbackEntry songFeedbackEntry){
		songFeedbackEntryMap.put(songId, songFeedbackEntry);
	}

	public void removeSongFromMap(String songId){
		songFeedbackEntryMap.remove(songId);
	}
	
	public boolean songExists(String songId){
		return songFeedbackEntryMap.containsKey(songId);
	}
	
	public void addFeedbackToSong(String songId, double feedback){
		songFeedbackEntryMap.get(songId).setFeedBack(feedback);
	}
	
	public double getFeedbackForSong(String songId){
		return songFeedbackEntryMap.get(songId).getFeedBack();
	}
	
	// Some more functions to be added later
	// getAllRatingsAbove(double rating);
	// getAverageRating();

	
	
	
	
}
