/**
 * 
 */
package com.desipandora.sessionInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.desipandora.api.SongEntry;

/**
 * @author abhishek
 *
 */
public class SessionEntry {
	String userId;
	long timeStamp;
	Map<String, SongFeedbackEntry> songFeedbackEntryMap;
	PlayList playList;
	
	

	public SessionEntry(String userId) {
		super();
		this.userId = userId;
		this.songFeedbackEntryMap = new HashMap<String, SongFeedbackEntry>();
		this.timeStamp = (new Date()).getTime();
		this.playList = new PlayList();
	}

	public void addPlayListToSession(PlayList playList){
		
		Iterator<SongEntry> iterPlayList = playList.getPlayList().iterator();
		this.playList.setPlayList(playList.getPlayList());
		this.playList.setRelatedFeedSeed(playList.getRelatedFeedSeed());
		while(iterPlayList.hasNext()){
			SongEntry songEntry = iterPlayList.next();
			songFeedbackEntryMap.put(songEntry.getSongId(), new SongFeedbackEntry(songEntry, 0.0));
		}
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
	public PlayList getPlayList() {
		return playList;
	}

	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}
	
	public void printPlayList(){
		List<SongEntry> songEntryList = playList.getPlayList();
		Iterator<SongEntry> iterSongEntryList = songEntryList.iterator();
		System.out.println("Playlist counterNextSeed is at "+playList.getRelatedFeedSeed());
		while(iterSongEntryList.hasNext()){
			System.out.println("PlayList title : " + iterSongEntryList.next().getTitle());
		}
	}
	// Some more functions to be added later
	// getAllRatingsAbove(double rating);
	// getAverageRating();

	
	
	
	
}
